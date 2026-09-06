package com.baitap.controller;

import com.baitap.model.User;
import com.baitap.otp.OtpPurpose;
import com.baitap.otp.OtpService;
import com.baitap.otp.OtpVerifyResult;
import com.baitap.service.UserService;
import com.baitap.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/forgot-password", "/forgot-password/verify", "/reset-password"})
public class ForgotPasswordController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    private final OtpService otpService = new OtpService();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/forgot-password/verify".equals(path) && !resetRequested(request)) { response.sendRedirect(request.getContextPath() + "/forgot-password"); return; }
        if ("/reset-password".equals(path) && (request.getSession(false) == null || request.getSession(false).getAttribute("passwordResetVerifiedUserId") == null)) { response.sendRedirect(request.getContextPath() + "/forgot-password"); return; }
        if ("/forgot-password/verify".equals(path) && "1".equals(request.getParameter("sent"))) request.setAttribute("message", "Nếu email tồn tại trong hệ thống, mã OTP đã được gửi.");
        request.getRequestDispatcher(viewFor(path)).forward(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        switch (request.getServletPath()) {
            case "/forgot-password" -> requestOtp(request, response);
            case "/forgot-password/verify" -> verifyOtp(request, response);
            case "/reset-password" -> resetPassword(request, response);
            default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void requestOtp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        clearResetSession(session);
        session.setAttribute("passwordResetRequested", Boolean.TRUE);
        User user = userService.findByEmail(request.getParameter("email") == null ? "" : request.getParameter("email").trim());
        if (user != null && user.isActive()) {
            session.setAttribute("passwordResetUserId", user.getId());
            session.setAttribute("passwordResetEmail", user.getEmail());
            try {
                otpService.send(user, OtpPurpose.PASSWORD_RESET);
            } catch (RuntimeException exception) {
                getServletContext().log("Password-reset OTP delivery failed for account id " + user.getId(), exception);
            }
        }
        response.sendRedirect(request.getContextPath() + "/forgot-password/verify?sent=1");
    }

    private void verifyOtp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer userId = resetUserId(request);
        if (!resetRequested(request)) { response.sendRedirect(request.getContextPath() + "/forgot-password"); return; }
        if ("resend".equals(request.getParameter("action"))) {
            try {
                if (userId != null) otpService.send(userService.findById(userId), OtpPurpose.PASSWORD_RESET);
                request.setAttribute("message", "Nếu email tồn tại trong hệ thống, mã OTP mới đã được gửi.");
            }
            catch (RuntimeException exception) {
                getServletContext().log("Password-reset OTP resend failed for account id " + userId, exception);
                request.setAttribute("error", "Chưa thể gửi email OTP. Vui lòng thử lại sau.");
            }
            request.getRequestDispatcher("/WEB-INF/views/forgot-password-verify.jsp").forward(request, response); return;
        }
        OtpVerifyResult result = userId == null ? OtpVerifyResult.INVALID : otpService.verify(userId, OtpPurpose.PASSWORD_RESET, request.getParameter("otp"));
        if (result == OtpVerifyResult.VERIFIED) {
            request.getSession().setAttribute("passwordResetVerifiedUserId", userId);
            request.getSession().setAttribute("passwordResetVerifiedEmail", request.getSession().getAttribute("passwordResetEmail"));
            response.sendRedirect(request.getContextPath() + "/reset-password"); return;
        }
        request.setAttribute("error", result == OtpVerifyResult.EXPIRED ? "Mã OTP đã hết hạn. Vui lòng gửi lại mã mới." : result == OtpVerifyResult.TOO_MANY_ATTEMPTS ? "Bạn đã nhập sai quá nhiều lần. Vui lòng gửi lại mã mới." : "Mã OTP không đúng.");
        request.getRequestDispatcher("/WEB-INF/views/forgot-password-verify.jsp").forward(request, response);
    }

    private void resetPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object value = session == null ? null : session.getAttribute("passwordResetVerifiedUserId");
        if (!(value instanceof Integer userId)) { response.sendRedirect(request.getContextPath() + "/forgot-password"); return; }
        String password = request.getParameter("password");
        if (password == null || !password.equals(request.getParameter("confirmPassword"))) {
            request.setAttribute("error", "Xác nhận mật khẩu chưa khớp."); request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(request, response); return;
        }
        try {
            userService.resetPassword(userId, password);
            clearResetSession(session);
            response.sendRedirect(request.getContextPath() + "/login?reset=1");
        } catch (RuntimeException exception) {
            request.setAttribute("error", exception.getMessage()); request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(request, response);
        }
    }

    private Integer resetUserId(HttpServletRequest request) { Object value = request.getSession(false) == null ? null : request.getSession(false).getAttribute("passwordResetUserId"); return value instanceof Integer id ? id : null; }
    private boolean resetRequested(HttpServletRequest request) { return request.getSession(false) != null && Boolean.TRUE.equals(request.getSession(false).getAttribute("passwordResetRequested")); }
    private void clearResetSession(HttpSession session) {
        if (session == null) return;
        session.removeAttribute("passwordResetRequested");
        session.removeAttribute("passwordResetUserId");
        session.removeAttribute("passwordResetEmail");
        session.removeAttribute("passwordResetVerifiedUserId");
        session.removeAttribute("passwordResetVerifiedEmail");
    }
    private String viewFor(String path) { return switch (path) { case "/forgot-password/verify" -> "/WEB-INF/views/forgot-password-verify.jsp"; case "/reset-password" -> "/WEB-INF/views/reset-password.jsp"; default -> "/WEB-INF/views/forgot-password.jsp"; }; }
}
