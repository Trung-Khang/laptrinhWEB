package com.baitap.controller;

import com.baitap.model.User;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/login"})
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Remember Me: nếu có cookie "username" thì tự động vào admin dashboard
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    resp.sendRedirect(req.getContextPath() + "/admin/category/list");
                    return;
                }
            }
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Kiểm tra dữ liệu đầu vào (tránh null / rỗng)
        if (isBlank(username) || isBlank(password)) {
            req.setAttribute("error", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        // Tìm user trong DB theo username (sử dụng PreparedStatement để chống SQL Injection)
        User user = userService.findByUsername(username);

        // Kiểm tra user tồn tại và mật khẩu khớp
        if (user != null && user.isActive() && user.getPassword().equals(password)) {
            // Đăng nhập thành công: lưu User vào session với attribute name "account"
            HttpSession session = req.getSession();
            session.setAttribute("account", user);

            // Remember Me: tạo cookie "username" với max age 30 phút (30 * 60 giây)
            String remember = req.getParameter("remember");
            if (remember != null) {
                Cookie cookie = new Cookie("username", username);
                cookie.setMaxAge(30 * 60);
                cookie.setHttpOnly(true); // giảm rủi ro truy cập cookie từ JavaScript
                cookie.setPath("/");
                resp.addCookie(cookie);
            }

            // Đăng nhập thành công -> chuyển thẳng tới admin dashboard (quản lý danh mục)
            resp.sendRedirect(req.getContextPath() + "/admin/category/list");
        } else {
            // Đăng nhập thất bại: đặt thông báo lỗi và forward về login.jsp
            req.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
            req.setAttribute("username", username); // giữ lại username để người dùng khỏi gõ lại
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

