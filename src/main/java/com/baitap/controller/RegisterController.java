package com.baitap.controller;

import com.baitap.model.User;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/register"})
public class RegisterController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");

        String error = null;

        // Kiểm tra bắt buộc nhập đầy đủ thông tin
        if (isBlank(username) || isBlank(password) || isBlank(email) || isBlank(fullname) || isBlank(phone)) {
            error = "Vui lòng điền đầy đủ tất cả các thông tin!";
        } else if (userService.checkExistUsername(username)) {
            error = "Tên đăng nhập đã tồn tại!";
        } else if (userService.checkExistEmail(email)) {
            error = "Email đã tồn tại!";
        } else if (userService.checkExistPhone(phone)) {
            error = "Số điện thoại đã tồn tại!";
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("username", username);
            req.setAttribute("email", email);
            req.setAttribute("fullname", fullname);
            req.setAttribute("phone", phone);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFullName(fullname);
        user.setPhone(phone);
        userService.insert(user);

        resp.sendRedirect(req.getContextPath() + "/login");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}