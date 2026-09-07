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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/profile")
public class ProfileController extends HttpServlet {
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
            userService.updateProfile(fresh);
            request.getSession().setAttribute("account", fresh);
            FlashMessage.success(request, "Cập nhật hồ sơ thành công.");
        } catch (RuntimeException exception) {
            FlashMessage.error(request, exception.getMessage() == null ? "Không thể cập nhật hồ sơ." : exception.getMessage());
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
