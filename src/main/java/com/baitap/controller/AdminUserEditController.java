package com.baitap.controller;

import com.baitap.model.User;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;
import com.baitap.util.FlashMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import vn.iotstar.validation.FormValidation;

@WebServlet(urlPatterns = "/admin/user/edit")
public class AdminUserEditController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = find(req.getParameter("id"));
        if (user == null) { FlashMessage.error(req, "Không tìm thấy người dùng."); resp.sendRedirect(req.getContextPath() + "/admin/user/list"); return; }
        req.setAttribute("editingUser", user); req.getRequestDispatcher("/views/admin/edit-user.jsp").forward(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); User stored = find(req.getParameter("id"));
        if (stored == null) { FlashMessage.error(req, "Không tìm thấy người dùng."); resp.sendRedirect(req.getContextPath() + "/admin/user/list"); return; }
        stored.setEmail(req.getParameter("email")); stored.setFullName(req.getParameter("fullname")); stored.setPhone(req.getParameter("phone"));
        stored.setPassword(req.getParameter("password")); stored.setActive(!"false".equals(req.getParameter("active")));
        try { stored.setRoleid(Integer.parseInt(req.getParameter("roleid"))); } catch (Exception e) { stored.setRoleid(0); }
        try {
            Map<String, String> errors = new LinkedHashMap<>();
            FormValidation.email(errors, "email", stored.getEmail()); FormValidation.maxLength(errors, "fullname", stored.getFullName(), 150, "Họ và tên"); FormValidation.optionalPhone(errors, "phone", stored.getPhone());
            if (!com.baitap.model.UserRole.isValid(stored.getRoleid())) errors.put("roleid", "Role không hợp lệ.");
            if (!errors.isEmpty()) { req.setAttribute("fieldErrors", errors); req.setAttribute("editingUser", stored); req.getRequestDispatcher("/views/admin/edit-user.jsp").forward(req, resp); return; }
            userService.updateByAdmin(stored);
            FlashMessage.success(req, "Cập nhật người dùng thành công.");
            resp.sendRedirect(req.getContextPath() + "/admin/user/list");
        } catch (RuntimeException e) {
            getServletContext().log("Edit user failed", e); req.setAttribute("error", e.getMessage()); req.setAttribute("editingUser", stored);
            req.getRequestDispatcher("/views/admin/edit-user.jsp").forward(req, resp);
        }
    }
    private User find(String value) { try { return userService.findById(Integer.parseInt(value)); } catch (Exception e) { return null; } }
}
