package com.baitap.otp;

import com.baitap.model.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class OtpService {
    private final AccountOtpDao otpDao = new AccountOtpDao();
    private final SmtpEmailService emailService = new SmtpEmailService();
    private final SecureRandom random = new SecureRandom();

    public void send(User user, OtpPurpose purpose) {
        if (user == null || user.getId() <= 0 || user.getEmail() == null || user.getEmail().isBlank()) {
            throw new IllegalArgumentException("Không tìm thấy email tài khoản.");
        }
        OtpIssueStatus status = otpDao.issueStatus(user.getId(), purpose);
        if (status == OtpIssueStatus.TOO_SOON) throw new IllegalStateException("Vui lòng chờ 60 giây trước khi gửi lại mã OTP.");
        if (status == OtpIssueStatus.RATE_LIMITED) throw new IllegalStateException("Bạn đã yêu cầu OTP quá nhiều lần. Vui lòng thử lại sau.");
        String code = String.format("%06d", random.nextInt(1_000_000));
        otpDao.create(user.getId(), purpose, sha256(code));
        emailService.sendOtp(user, purpose, code);
    }

    public OtpVerifyResult verify(int userId, OtpPurpose purpose, String code) {
        if (code == null || !code.matches("\\d{6}")) return OtpVerifyResult.INVALID;
        return otpDao.verify(userId, purpose, sha256(code));
    }

    private String sha256(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder(64);
            for (byte current : digest) result.append(String.format("%02x", current));
            return result.toString();
        } catch (NoSuchAlgorithmException exception) { throw new IllegalStateException("Không hỗ trợ SHA-256.", exception); }
    }
}
