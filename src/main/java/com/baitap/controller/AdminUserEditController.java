package com.baitap.controller;

import java.io.IOException;

import com.baitap.model.User;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/admin/user/edit")
public class AdminUserEditController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = parseInt(req.getParameter("id"));
        req.setAttribute("user", id == null ? null : userService.findById(id));
        req.getRequestDispatcher("/views/admin/edit-user.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = userService.findById(parseInt(req.getParameter("id")));
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/user/list?error=Không tìm thấy người dùng");
            return;
        }
        user.setEmail(req.getParameter("email"));
        user.setFullName(req.getParameter("fullname"));
        user.setPhone(req.getParameter("phone"));
        user.setRoleid(Integer.parseInt(req.getParameter("roleid")));
        user.setActive(Boolean.parseBoolean(req.getParameter("active")));
        userService.update(user);
        resp.sendRedirect(req.getContextPath() + "/admin/user/list?message=Cập nhật người dùng thành công");
    }

    private Integer parseInt(String value) {
        try { return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value); }
        catch (NumberFormatException e) { return null; }
    }
}
