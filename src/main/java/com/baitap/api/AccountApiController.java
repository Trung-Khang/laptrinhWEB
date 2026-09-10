package com.baitap.api;

import com.baitap.model.User;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;
import com.baitap.storefront.StorefrontRepository;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import vn.iotstar.entity.Order;
import vn.iotstar.validation.FormValidation;
import vn.iotstar.validation.ImageUploadUtil;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 2 * 1024 * 1024, maxRequestSize = 5 * 1024 * 1024)
@WebServlet("/api/account/*")
public class AccountApiController extends BaseApiServlet {
    private static final long MAX_AVATAR_BYTES = 2L * 1024 * 1024;
    private final UserService userService = new UserServiceImpl();
    private final StorefrontRepository repository = new StorefrontRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = requireUser(request, response);
        if (user == null) return;
        try {
            String path = request.getPathInfo();
            if ("/profile".equals(path)) {
                User fresh = userService.findById(user.getId());
                if (fresh == null || !fresh.isActive()) {
                    error(response, 401, "Vui lòng đăng nhập để tiếp tục.");
                    return;
                }
                request.getSession().setAttribute("account", fresh);
                ok(response, StorefrontMapper.profile(fresh, request.getContextPath()));
                return;
            }
            if ("/orders".equals(path)) {
                ok(response, repository.ordersForUser(user.getId()).stream().map(StorefrontMapper::order).toList());
                return;
            }
            int orderId = orderId(path);
            Order order = repository.orderForUser(orderId, user.getId());
            if (order == null) {
                error(response, 404, "Không tìm thấy đơn hàng.");
                return;
            }
            ok(response, StorefrontMapper.order(order));
        } catch (IllegalArgumentException exception) {
            error(response, 400, exception.getMessage());
        } catch (RuntimeException exception) {
            getServletContext().log("Account API failed", exception);
            error(response, 500, "Không thể tải thông tin tài khoản.");
        }
    }

    /** Keeps the existing JSON update contract for clients that do not upload an avatar. */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = requireUser(request, response);
        if (user == null) return;
        try {
            String path = request.getPathInfo();
            if ("/profile".equals(path)) {
                JsonObject body = readBody(request);
                String fullName = string(body, "fullName");
                String email = string(body, "email");
                String phone = string(body, "phone");
                Map<String, String> fieldErrors = validateProfile(fullName, email, phone, true);
                if (!fieldErrors.isEmpty()) {
                    error(response, 400, "Dữ liệu hồ sơ chưa hợp lệ.", fieldErrors);
                    return;
                }
                User fresh = requiredFreshUser(user);
                fresh.setFullName(fullName);
                fresh.setEmail(email);
                fresh.setPhone(phone);
                userService.updateProfile(fresh);
                request.getSession().setAttribute("account", fresh);
                ok(response, StorefrontMapper.profile(fresh, request.getContextPath()));
                return;
            }
            if (path != null && path.endsWith("/cancel")) {
                int id = orderId(path.substring(0, path.length() - "/cancel".length()));
                repository.cancelOrder(id, user.getId());
                ok(response, StorefrontMapper.order(repository.orderForUser(id, user.getId())));
                return;
            }
            error(response, 404, "Không tìm thấy API tài khoản.");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            error(response, 400, exception.getMessage());
        } catch (RuntimeException exception) {
            getServletContext().log("Account update failed", exception);
            error(response, 500, "Không thể cập nhật thông tin.");
        }
    }

    /** Multipart endpoint used by the React profile form for fullname, phone and avatar. */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = requireUser(request, response);
        if (user == null) return;
        if (!"/profile".equals(request.getPathInfo())) {
            error(response, 404, "Không tìm thấy API tài khoản.");
            return;
        }

        Map<String, String> fieldErrors = new LinkedHashMap<>();
        Part avatarPart = null;
        try {
            avatarPart = request.getPart("avatar");
        } catch (IllegalStateException | ServletException exception) {
            fieldErrors.put("avatar", "Ảnh đại diện không được vượt quá 2 MB.");
        }
        String fullName = FormValidation.trim(request.getParameter("fullName"));
        String phone = FormValidation.trim(request.getParameter("phone"));
        fieldErrors.putAll(validateProfile(fullName, null, phone, false));
        FormValidation.validateAvatar(fieldErrors, "avatar", avatarPart, MAX_AVATAR_BYTES);
        if (!fieldErrors.isEmpty()) {
            error(response, 400, "Dữ liệu hồ sơ chưa hợp lệ.", fieldErrors);
            return;
        }

        String savedAvatar = null;
        try {
            User fresh = requiredFreshUser(user);
            fresh.setFullName(fullName);
            fresh.setPhone(phone);
            if (avatarPart != null && avatarPart.getSize() > 0) {
                savedAvatar = ImageUploadUtil.save(avatarPart, "avatar");
                fresh.setAvatar(savedAvatar);
            }
            userService.updateProfile(fresh);
            request.getSession().setAttribute("account", fresh);
            ok(response, StorefrontMapper.profile(fresh, request.getContextPath()));
        } catch (IllegalArgumentException | IllegalStateException exception) {
            ImageUploadUtil.deleteQuietly(savedAvatar);
            error(response, 400, exception.getMessage());
        } catch (RuntimeException | IOException exception) {
            ImageUploadUtil.deleteQuietly(savedAvatar);
            getServletContext().log("Profile avatar upload failed", exception);
            error(response, 500, "Không thể lưu ảnh đại diện. Vui lòng thử lại.");
        }
    }

    private Map<String, String> validateProfile(String fullName, String email, String phone, boolean validateEmail) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        FormValidation.required(fieldErrors, "fullName", fullName, "họ và tên");
        FormValidation.maxLength(fieldErrors, "fullName", fullName, 150, "Họ và tên");
        if (validateEmail) {
            FormValidation.email(fieldErrors, "email", email);
        }
        FormValidation.optionalPhone(fieldErrors, "phone", phone);
        return fieldErrors;
    }

    private User requiredFreshUser(User sessionUser) {
        User fresh = userService.findById(sessionUser.getId());
        if (fresh == null || !fresh.isActive()) {
            throw new IllegalStateException("Tài khoản không còn hoạt động.");
        }
        return fresh;
    }

    private User requireUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = currentUser(request);
        if (user == null) error(response, 401, "Vui lòng đăng nhập để tiếp tục.");
        return user;
    }

    private int orderId(String path) {
        try {
            String[] parts = path.split("/");
            return Integer.parseInt(parts[2]);
        } catch (Exception exception) {
            throw new IllegalArgumentException("Mã đơn hàng không hợp lệ.");
        }
    }
}
