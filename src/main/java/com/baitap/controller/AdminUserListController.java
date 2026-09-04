package com.baitap.controller;

import java.io.IOException;

import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = "/admin/user/list")
public class AdminUserListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        Integer roleid = parseInt(req.getParameter("roleid"));
        Boolean active = parseBool(req.getParameter("active"));
        req.setAttribute("users", userService.search(keyword, roleid, active));
        req.getRequestDispatcher("/views/admin/list-user.jsp").forward(req, resp);
    }

    private Integer parseInt(String value) {
        try { return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value); }
        catch (NumberFormatException e) { return null; }
    }

    private Boolean parseBool(String value) {
        return value == null || value.trim().isEmpty() ? null : Boolean.valueOf(value);
    }
}
