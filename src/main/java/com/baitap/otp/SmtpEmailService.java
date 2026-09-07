package com.baitap.otp;

import com.baitap.model.User;
import jakarta.mail.Authenticator;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.SendFailedException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/** SMTP secrets are read only from local runtime configuration, never from source or logs. */
public class SmtpEmailService {
    private static final Logger LOGGER = Logger.getLogger(SmtpEmailService.class.getName());
    private static final Map<String, String> SETENV_VALUES = loadSetenvValues();

    public void sendOtp(User user, OtpPurpose purpose, String otp) {
        String host = environment("SMTP_HOST");
        String username = environment("SMTP_USERNAME");
        String password = environment("SMTP_PASSWORD");
        String from = environment("SMTP_FROM");
        String startTlsValue = environment("SMTP_STARTTLS");
        List<String> missing = missingConfiguration(host, username, password, from, startTlsValue);
        if (!missing.isEmpty()) {
            LOGGER.warning("SMTP configuration is incomplete. Missing variables: " + String.join(", ", missing));
            throw new IllegalStateException("Chưa cấu hình SMTP để gửi email. Vui lòng thử lại sau.");
        }
        String port = blank(environment("SMTP_PORT")) ? "587" : environment("SMTP_PORT");
        if (!"true".equalsIgnoreCase(startTlsValue)) {
            LOGGER.warning("SMTP_STARTTLS must be true for Gmail SMTP.");
            throw new IllegalStateException("Cấu hình SMTP chưa hợp lệ. Vui lòng thử lại sau.");
        }
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host); properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", host);
        properties.put("mail.smtp.connectiontimeout", "10000");
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.writetimeout", "10000");
        Authenticator authenticator = new Authenticator() {
            @Override protected PasswordAuthentication getPasswordAuthentication() { return new PasswordAuthentication(username, password); }
        };
        try {
            MimeMessage message = new MimeMessage(Session.getInstance(properties, authenticator));
            message.setFrom(new InternetAddress(from, "KhangGear", "UTF-8"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(), false));
            boolean registration = purpose == OtpPurpose.REGISTER_VERIFY;
            message.setSubject(registration ? "[KHANGGEAR] Mã xác nhận tạo tài khoản" : "[KHANGGEAR] Mã xác nhận đặt lại mật khẩu", "UTF-8");
            message.setContent(html(user.getFullName(), otp, registration), "text/html; charset=UTF-8");
            Transport.send(message);
            LOGGER.info("OTP email delivered for account id " + user.getId() + " and purpose " + purpose.name());
        } catch (AuthenticationFailedException exception) {
            logDeliveryFailure("Gmail authentication failed", user, purpose, exception);
            throw new IllegalStateException("Không thể gửi email OTP. Vui lòng thử lại sau.", exception);
        } catch (SendFailedException exception) {
            logDeliveryFailure("Recipient address rejected", user, purpose, exception);
            throw new IllegalStateException("Không thể gửi email OTP. Vui lòng thử lại sau.", exception);
        } catch (MessagingException exception) {
            logDeliveryFailure(mailFailureCategory(exception), user, purpose, exception);
            throw new IllegalStateException("Không thể gửi email OTP. Vui lòng thử lại sau.", exception);
        } catch (RuntimeException exception) {
            logDeliveryFailure("Unexpected mail runtime failure", user, purpose, exception);
            throw new IllegalStateException("Không thể gửi email OTP. Vui lòng thử lại sau.", exception);
        } catch (Exception exception) {
            logDeliveryFailure("SMTP message encoding failure", user, purpose, exception);
            throw new IllegalStateException("Không thể gửi email OTP. Vui lòng thử lại sau.", exception);
        }
    }

    private String environment(String name) {
        String value = System.getenv(name);
        if (blank(value)) value = System.getProperty(name);
        if (blank(value)) value = SETENV_VALUES.get(name);
        return value == null ? "" : value.trim();
    }
    private boolean blank(String value) { return value == null || value.isBlank(); }

    private static Map<String, String> loadSetenvValues() {
        Map<String, String> values = new HashMap<>();
        String base = System.getProperty("catalina.base");
        if (base == null || base.isBlank()) return values;
        Path setenv = Path.of(base, "bin", "setenv.bat");
        if (!Files.isRegularFile(setenv)) return values;
        try {
            for (String line : Files.readAllLines(setenv, StandardCharsets.UTF_8)) {
                String trimmed = line.trim();
                if (!trimmed.regionMatches(true, 0, "set ", 0, 4)) continue;
                String assignment = trimmed.substring(4).trim();
                if (assignment.startsWith("\"") && assignment.endsWith("\"")) assignment = assignment.substring(1, assignment.length() - 1);
                int equals = assignment.indexOf('=');
                if (equals <= 0) continue;
                String key = assignment.substring(0, equals).trim();
                if (!key.startsWith("SMTP_")) continue;
                values.put(key, assignment.substring(equals + 1).trim());
            }
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, "Cannot read local SMTP runtime configuration.", exception);
        }
        return values;
    }

    private List<String> missingConfiguration(String host, String username, String password, String from, String startTls) {
        List<String> missing = new ArrayList<>();
        if (blank(host)) missing.add("SMTP_HOST");
        if (blank(username)) missing.add("SMTP_USERNAME");
        if (blank(password)) missing.add("SMTP_PASSWORD");
        if (blank(from)) missing.add("SMTP_FROM");
        if (blank(startTls)) missing.add("SMTP_STARTTLS");
        return missing;
    }

    private String mailFailureCategory(MessagingException exception) {
        String detail = String.valueOf(exception.getMessage()).toLowerCase();
        if (detail.contains("starttls") || detail.contains("tls")) return "STARTTLS negotiation failed";
        if (detail.contains("connect") || detail.contains("timeout")) return "SMTP connection failed";
        return "SMTP messaging failure";
    }

    private void logDeliveryFailure(String category, User user, OtpPurpose purpose, Exception exception) {
        LOGGER.log(Level.WARNING, category + " for account id " + user.getId() + " and purpose " + purpose.name(), exception);
    }

    private String html(String fullName, String otp, boolean registration) {
        String recipient = blank(fullName) ? "bạn" : escape(fullName);
        String action = registration ? "kích hoạt tài khoản" : "đặt lại mật khẩu";
        return "<!doctype html><html lang=\"vi\"><body style=\"margin:0;background:#edf3fa;font-family:Arial,sans-serif;color:#10233f\">"
                + "<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"><tr><td style=\"padding:28px 12px\"><table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width:560px;margin:auto;background:#ffffff;border-radius:10px;overflow:hidden\">"
                + "<tr><td style=\"padding:28px;background:#071b35;color:#ffffff;text-align:center;font-size:28px;font-weight:700;letter-spacing:1px\">KhangGear</td></tr>"
                + "<tr><td style=\"padding:32px\"><h1 style=\"margin:0 0 16px;font-size:22px\">Mã xác nhận " + action + "</h1>"
                + "<p>Chào " + recipient + ",</p><p>Đây là mã OTP để " + action + " tại KhangGear:</p>"
                + "<p style=\"margin:24px 0;padding:16px;background:#eaf4ff;border:1px solid #b8ddff;border-radius:8px;text-align:center;font-size:32px;font-weight:700;letter-spacing:8px;color:#0878e8\">" + otp + "</p>"
                + "<p>Mã có hiệu lực trong <strong>5 phút</strong> và chỉ sử dụng một lần.</p><p style=\"color:#b42318\"><strong>Không chia sẻ mã OTP này cho bất kỳ ai.</strong></p>"
                + "</td></tr><tr><td style=\"padding:16px 32px;background:#f4f7fb;color:#52657d;font-size:12px\">Email được gửi tự động từ KhangGear.</td></tr>"
                + "</table></td></tr></table></body></html>";
    }

    private String escape(String value) { return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;"); }
}
