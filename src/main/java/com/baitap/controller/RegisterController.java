package com.baitap.controller;

import com.baitap.model.User;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;
import com.baitap.otp.OtpPurpose;
import com.baitap.otp.OtpService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    private final OtpService otpService = new OtpService();
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = new User();
        user.setUserName(req.getParameter("username")); user.setPassword(req.getParameter("password"));
        user.setEmail(req.getParameter("email")); user.setFullName(req.getParameter("fullname")); user.setPhone(req.getParameter("phone"));
        try {
            User existing = userService.findByEmail(user.getEmail() == null ? "" : user.getEmail().trim());
            if (existing != null) {
                if (!existing.isEmailVerified()) {
                    openVerification(req, resp, existing);
                    return;
                }
                throw new IllegalArgumentException("Email đã tồn tại.");
            }
            userService.registerPublic(user);
            openVerification(req, resp, user);
        } catch (RuntimeException e) {
            getServletContext().log("Register failed", e);
            req.setAttribute("error", e.getMessage()); req.setAttribute("formUser", user);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }
    }

    private void openVerification(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        request.getSession(true).setAttribute("registrationUserId", user.getId());
        try {
            otpService.send(user, OtpPurpose.REGISTER_VERIFY);
            response.sendRedirect(request.getContextPath() + "/verify-email?sent=1");
        } catch (RuntimeException emailError) {
            // Technical details are retained for administrators; neither password nor OTP is ever logged.
            getServletContext().log("Registration OTP delivery failed for account id " + user.getId(), emailError);
            response.sendRedirect(request.getContextPath() + "/verify-email?delivery=failed");
        }
    }
}
