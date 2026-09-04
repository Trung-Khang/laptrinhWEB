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

@WebServlet(urlPatterns = "/admin/user/add")
public class AdminUserAddController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/admin/add-user.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            User user = new User();
            user.setUserName(req.getParameter("username"));
            user.setPassword(req.getParameter("password"));
            user.setEmail(req.getParameter("email"));
            user.setFullName(req.getParameter("fullname"));
            user.setPhone(req.getParameter("phone"));
            user.setRoleid(Integer.parseInt(req.getParameter("roleid")));
            user.setActive(Boolean.parseBoolean(req.getParameter("active")));
            if (isBlank(user.getUserName()) || isBlank(user.getPassword()) || isBlank(user.getEmail())) {
                throw new IllegalArgumentException("Vui lòng nhập username, email và mật khẩu.");
            }
            if (userService.checkExistUsername(user.getUserName())) throw new IllegalArgumentException("Username đã tồn tại.");
            if (userService.checkExistEmail(user.getEmail())) throw new IllegalArgumentException("Email đã tồn tại.");
            userService.insert(user);
            resp.sendRedirect(req.getContextPath() + "/admin/user/list?message=Thêm người dùng thành công");
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/views/admin/add-user.jsp").forward(req, resp);
        }
    }

    private boolean isBlank(String v) { return v == null || v.trim().isEmpty(); }
}
