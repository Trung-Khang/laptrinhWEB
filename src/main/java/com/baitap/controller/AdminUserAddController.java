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

@WebServlet(urlPatterns = "/admin/user/add")
public class AdminUserAddController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { req.getRequestDispatcher("/views/admin/add-user.jsp").forward(req, resp); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8"); User user = fromRequest(req);
        try {
            userService.createByAdmin(user);
            FlashMessage.success(req, "Thêm người dùng thành công.");
            resp.sendRedirect(req.getContextPath() + "/admin/user/list");
        } catch (RuntimeException e) {
            getServletContext().log("Add user failed", e); req.setAttribute("error", e.getMessage()); req.setAttribute("formUser", user);
            req.getRequestDispatcher("/views/admin/add-user.jsp").forward(req, resp);
        }
    }
    private User fromRequest(HttpServletRequest req) {
        User user = new User(); user.setUserName(req.getParameter("username")); user.setPassword(req.getParameter("password"));
        user.setEmail(req.getParameter("email")); user.setFullName(req.getParameter("fullname")); user.setPhone(req.getParameter("phone"));
        try { user.setRoleid(Integer.parseInt(req.getParameter("roleid"))); } catch (Exception e) { user.setRoleid(0); }
        user.setActive(!"false".equals(req.getParameter("active"))); return user;
    }
}
