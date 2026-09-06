package com.baitap.controller;

import com.baitap.model.User;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;
import com.baitap.util.LoginRedirect;
import com.baitap.security.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) req.setAttribute("rememberedUsername", cookie.getValue());
            }
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (blank(username) || blank(password)) { showError(req, resp, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!", username); return; }
        try {
            User user = userService.findByUsername(username);
            if (user == null || !PasswordUtil.matches(password, user.getPassword())) {
                showError(req, resp, "Sai tên đăng nhập, mật khẩu hoặc tài khoản đã bị khóa!", username);
                return;
            }
            if (!user.isEmailVerified()) {
                req.getSession(true).setAttribute("registrationUserId", user.getId());
                req.setAttribute("verificationRequired", true);
                showError(req, resp, "Tài khoản chưa được xác minh. Vui lòng kiểm tra email để nhập mã OTP.", username);
                return;
            }
            if (!user.isActive()) {
                showError(req, resp, "Tài khoản đã bị khóa. Vui lòng liên hệ quản trị viên.", username);
                return;
            }
            HttpSession oldSession = req.getSession(false);
            if (oldSession != null) oldSession.invalidate();
            HttpSession session = req.getSession(true);
            session.setAttribute("account", user);
            if (req.getParameter("remember") != null) {
                Cookie cookie = new Cookie("username", user.getUserName());
                cookie.setMaxAge(30 * 60); cookie.setHttpOnly(true);
                cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
                resp.addCookie(cookie);
            }
            resp.sendRedirect(LoginRedirect.pathForRole(req.getContextPath(), user.getRoleid()));
        } catch (RuntimeException e) {
            getServletContext().log("Login database error", e);
            showError(req, resp, "Không thể kết nối cơ sở dữ liệu. Vui lòng thử lại sau.", username);
        }
    }

    private void showError(HttpServletRequest req, HttpServletResponse resp, String error, String username) throws ServletException, IOException {
        req.setAttribute("error", error); req.setAttribute("username", username);
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
    private boolean blank(String value) { return value == null || value.trim().isEmpty(); }
}
