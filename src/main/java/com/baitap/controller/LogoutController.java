package com.baitap.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogoutController extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException { logout(req, resp); }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException { logout(req, resp); }
    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false); if (session != null) session.invalidate();
        Cookie cookie = new Cookie("username", ""); cookie.setMaxAge(0); cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath()); resp.addCookie(cookie);
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
