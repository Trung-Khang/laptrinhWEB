package com.baitap.controller;

import com.baitap.model.User;
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
import vn.iotstar.util.Constant;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 MB
        maxFileSize = 1024 * 1024 * 2,        // 2 MB
        maxRequestSize = 1024 * 1024 * 5)     // 5 MB
@WebServlet(urlPatterns = "/profile")
public class ProfileController extends HttpServlet {
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg",
            ".jpeg",
            ".png",
            ".webp"
    );
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User account = currentAccount(request, response);
        if (account == null) return;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User account = currentAccount(request, response);
        if (account == null) return;
        try {
            User fresh = userService.findById(account.getId());
            if (fresh == null || !fresh.isActive()) {
                response.sendRedirect(request.getContextPath() + "/logout");
                return;
            }
            fresh.setFullName(request.getParameter("fullname"));
            fresh.setPhone(request.getParameter("phone"));

            Part avatarPart = null;
            try {
                avatarPart = request.getPart("avatar");
            } catch (IllegalStateException | ServletException e) {
                throw new IllegalArgumentException("Dung lượng ảnh vượt quá giới hạn 2MB.");
            }

            if (avatarPart != null && avatarPart.getSize() > 0) {
                if (avatarPart.getSize() > MAX_FILE_SIZE) {
                    throw new IllegalArgumentException("Kích thước ảnh đại diện không được vượt quá 2MB.");
                }

                String mimeType = avatarPart.getContentType();
                if (mimeType == null || !ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase())) {
                    throw new IllegalArgumentException("Chỉ chấp nhận các định dạng ảnh JPG, PNG hoặc WEBP.");
                }

                String submitted = avatarPart.getSubmittedFileName();
                if (submitted == null || submitted.isBlank()) {
                    throw new IllegalArgumentException("Tên file tải lên không hợp lệ.");
                }
                String cleanName = Paths.get(submitted).getFileName().toString();
                int dotIndex = cleanName.lastIndexOf('.');
                String ext = dotIndex >= 0 ? cleanName.substring(dotIndex).toLowerCase() : "";
                if (!ALLOWED_EXTENSIONS.contains(ext)) {
                    throw new IllegalArgumentException("Phần mở rộng file không hợp lệ (.jpg, .jpeg, .png, .webp).");
                }

                String safeFileName = "avatar_" + account.getId() + "_" + UUID.randomUUID().toString().replace("-", "") + ext;
                String uploadDir = Constant.DIR + File.separator + "avatar";
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File targetFile = new File(dir, safeFileName);
                if (!targetFile.toPath().normalize().startsWith(dir.toPath().normalize())) {
                    throw new IllegalArgumentException("Phát hiện đường dẫn không an toàn (path traversal).");
                }

                avatarPart.write(targetFile.getAbsolutePath());
                fresh.setAvatar("avatar/" + safeFileName);
            }

            userService.updateProfile(fresh);
            request.getSession().setAttribute("account", fresh);
            FlashMessage.success(request, "Cập nhật hồ sơ thành công.");
        } catch (Exception exception) {
            String msg = exception.getMessage();
            if (msg == null || msg.isBlank()) {
                msg = "Không thể cập nhật hồ sơ hoặc file tải lên quá giới hạn cho phép.";
            }
            FlashMessage.error(request, msg);
        }
        response.sendRedirect(request.getContextPath() + "/profile");
    }

    private User currentAccount(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        Object value = session == null ? null : session.getAttribute("account");
        if (value instanceof User user) {
            return user;
        }
        response.sendRedirect(request.getContextPath() + "/login");
        return null;
    }
}
