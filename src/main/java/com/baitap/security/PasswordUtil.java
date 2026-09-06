package com.baitap.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.mindrot.jbcrypt.BCrypt;

/** Supports legacy plaintext accounts while all new password writes use BCrypt. */
public final class PasswordUtil {
    private PasswordUtil() { }

    public static String hash(String password) {
        if (password == null) throw new IllegalArgumentException("Mật khẩu không hợp lệ.");
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean matches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) return false;
        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            try { return BCrypt.checkpw(rawPassword, storedPassword); }
            catch (IllegalArgumentException ignored) { return false; }
        }
        return MessageDigest.isEqual(rawPassword.getBytes(StandardCharsets.UTF_8), storedPassword.getBytes(StandardCharsets.UTF_8));
    }
}
