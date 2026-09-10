package com.baitap.controller;

import com.baitap.model.User;
import com.baitap.model.UserRole;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;
import com.baitap.util.FlashMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import vn.iotstar.validation.FormValidation;
import vn.iotstar.validation.ImageUploadUtil;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 2 * 1024 * 1024, maxRequestSize = 5 * 1024 * 1024)
@WebServlet(urlPatterns = "/profile")
public class ProfileController extends HttpServlet {
    private static final long MAX_FILE_SIZE = 2L * 1024 * 1024;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User account = currentAccount(request, response);
        if (account == null) return;
        if (UserRole.canAccessAdmin(account.getRoleid())) {
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
            return;
        }
        FlashMessage.expose(request);
        User fresh = userService.findById(account.getId());
        if (fresh == null || !fresh.isActive()) {
            response.sendRedirect(request.getContextPath() + "/logout");
            return;
        }
        request.getSession().setAttribute("account", fresh);
        request.setAttribute("profileUser", fresh);
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User account = currentAccount(request, response);
        if (account == null) return;
        if (UserRole.canAccessAdmin(account.getRoleid())) {
            response.sendRedirect(request.getContextPath() + "/admin/category/list");
            return;
        }
        User fresh = userService.findById(account.getId());
        if (fresh == null || !fresh.isActive()) {
            response.sendRedirect(request.getContextPath() + "/logout");
            return;
        }

        String fullName = FormValidation.trim(request.getParameter("fullname"));
        String phone = FormValidation.trim(request.getParameter("phone"));
        Map<String, String> errors = new LinkedHashMap<>();
        FormValidation.required(errors, "fullname", fullName, "họ và tên");
        FormValidation.maxLength(errors, "fullname", fullName, 150, "Họ và tên");
        FormValidation.optionalPhone(errors, "phone", phone);

        Part avatarPart = null;
        try {
            avatarPart = request.getPart("avatar");
        } catch (IllegalStateException | ServletException exception) {
            errors.put("avatar", "Ảnh đại diện không được vượt quá 2 MB.");
        }
        FormValidation.validateAvatar(errors, "avatar", avatarPart, MAX_FILE_SIZE);
        fresh.setFullName(fullName);
        fresh.setPhone(phone);
        if (!errors.isEmpty()) {
            showFormErrors(request, response, fresh, errors);
            return;
        }

        String savedAvatar = null;
        try {
            if (avatarPart != null && avatarPart.getSize() > 0) {
                savedAvatar = ImageUploadUtil.save(avatarPart, "avatar");
                fresh.setAvatar(savedAvatar);
            }
            userService.updateProfile(fresh);
            request.getSession().setAttribute("account", fresh);
            FlashMessage.success(request, "Cập nhật hồ sơ thành công.");
        } catch (RuntimeException | IOException exception) {
            ImageUploadUtil.deleteQuietly(savedAvatar);
            getServletContext().log("Profile update failed", exception);
            FlashMessage.error(request, "Không thể cập nhật hồ sơ. Vui lòng thử lại.");
        }
        response.sendRedirect(request.getContextPath() + "/profile");
    }

    private void showFormErrors(HttpServletRequest request, HttpServletResponse response, User user,
                                Map<String, String> errors) throws ServletException, IOException {
        request.setAttribute("profileUser", user);
        request.setAttribute("fieldErrors", errors);
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    private User currentAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Object value = session == null ? null : session.getAttribute("account");
        if (value instanceof User user) return user;
        response.sendRedirect(request.getContextPath() + "/login");
        return null;
    }
}
