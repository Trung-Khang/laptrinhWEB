package com.baitap.util;

import com.baitap.model.UserRole;

public final class LoginRedirect {
    private LoginRedirect() {
    }

    public static String pathForRole(String contextPath, int roleId) {
        if (roleId == UserRole.ADMIN || roleId == UserRole.MANAGER) {
            return contextPath + "/admin/category/list";
        }
        return contextPath + "/home";
    }
}
