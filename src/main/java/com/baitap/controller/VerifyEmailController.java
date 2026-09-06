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
import java.io.IOException;

@WebServlet(urlPatterns = "/verify-email")
public class VerifyEmailController extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    private final OtpService otpService = new OtpService();

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false) == null || request.getSession(false).getAttribute("registrationUserId") == null) {
            response.sendRedirect(request.getContextPath() + "/login"); return;
        }
        if ("sent".equals(request.getParameter("sent"))) request.setAttribute("message", "Mã xác nhận đã được gửi đến email của bạn.");
        if ("failed".equals(request.getParameter("delivery"))) request.setAttribute("error", "Tài khoản đã được tạo nhưng chưa thể gửi email xác nhận. Vui lòng thử gửi lại OTP sau.");
        request.getRequestDispatcher("/WEB-INF/views/verify-email.jsp").forward(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Object value = request.getSession().getAttribute("registrationUserId");
        if (!(value instanceof Integer userId)) { response.sendRedirect(request.getContextPath() + "/login"); return; }
        User user = userService.findById(userId);
        if (user == null || user.isEmailVerified()) { response.sendRedirect(request.getContextPath() + "/login"); return; }
        if ("resend".equals(request.getParameter("action"))) {
            try { otpService.send(user, OtpPurpose.REGISTER_VERIFY); request.setAttribute("message", "Mã xác nhận đã được gửi đến email của bạn."); }
            catch (RuntimeException exception) {
                getServletContext().log("Registration OTP resend failed for account id " + userId, exception);
                request.setAttribute("error", "Tài khoản đã được tạo nhưng chưa thể gửi email xác nhận. Vui lòng thử gửi lại OTP sau.");
            }
            request.getRequestDispatcher("/WEB-INF/views/verify-email.jsp").forward(request, response); return;
        }
        OtpVerifyResult result = otpService.verify(userId, OtpPurpose.REGISTER_VERIFY, request.getParameter("otp"));
        if (result == OtpVerifyResult.VERIFIED) {
            request.getSession().removeAttribute("registrationUserId");
            response.sendRedirect(request.getContextPath() + "/login?verified=1"); return;
        }
        request.setAttribute("error", messageFor(result));
        request.getRequestDispatcher("/WEB-INF/views/verify-email.jsp").forward(request, response);
    }

    private String messageFor(OtpVerifyResult result) {
        return switch (result) { case EXPIRED -> "Mã OTP đã hết hạn. Vui lòng gửi lại mã mới."; case TOO_MANY_ATTEMPTS -> "Bạn đã nhập sai quá nhiều lần. Vui lòng gửi lại mã mới."; default -> "Mã OTP không đúng. Vui lòng kiểm tra lại."; };
    }
}
