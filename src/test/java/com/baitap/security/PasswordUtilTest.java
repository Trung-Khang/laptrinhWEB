package com.baitap.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class PasswordUtilTest {
    @Test
    void acceptsLegacyPlaintextAndRejectsWrongValue() {
        assertTrue(PasswordUtil.matches("123", "123"));
        assertFalse(PasswordUtil.matches("wrong", "123"));
    }

    @Test
    void hashesNewPasswordWithBcrypt() {
        String hash = PasswordUtil.hash("new-password");
        assertTrue(hash.startsWith("$2"));
        assertTrue(PasswordUtil.matches("new-password", hash));
        assertFalse(PasswordUtil.matches("old-password", hash));
    }
}
