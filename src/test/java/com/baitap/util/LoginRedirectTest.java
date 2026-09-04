package com.baitap.util;

import com.baitap.model.UserRole;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class LoginRedirectTest {
    @Test
    void redirectsAdminAndManagerToAllowedAdminStartPage() {
        assertEquals("/dangnhap/admin/category/list", LoginRedirect.pathForRole("/dangnhap", UserRole.ADMIN));
        assertEquals("/dangnhap/admin/category/list", LoginRedirect.pathForRole("/dangnhap", UserRole.MANAGER));
    }

    @Test
    void redirectsCustomerOutsideAdmin() {
        assertEquals("/dangnhap/home", LoginRedirect.pathForRole("/dangnhap", UserRole.CUSTOMER));
    }

    @Test
    void hasOneRoleConvention() {
        assertTrue(UserRole.canAccessAdmin(UserRole.ADMIN));
        assertTrue(UserRole.canAccessAdmin(UserRole.MANAGER));
        assertFalse(UserRole.canAccessAdmin(UserRole.CUSTOMER));
        assertFalse(UserRole.isValid(99));
    }
}
