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

@WebServlet(urlPatterns = "/admin/user/toggle")
public class AdminUserToggleController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User account = (User) req.getSession().getAttribute("account");
        int id = Integer.parseInt(req.getParameter("id"));
        if (account != null && account.getId() == id) {
            resp.sendRedirect(req.getContextPath() + "/admin/user/list?error=Không thể tự khóa tài khoản đang đăng nhập");
            return;
        }
        userService.updateActive(id, Boolean.parseBoolean(req.getParameter("active")));
        resp.sendRedirect(req.getContextPath() + "/admin/user/list?message=Cập nhật trạng thái thành công");
    }
}
