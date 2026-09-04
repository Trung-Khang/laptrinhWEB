package com.baitap.model;

/** Role values shared by login, filters and user administration. */
public final class UserRole {
    public static final int ADMIN = 1;
    public static final int MANAGER = 2;
    public static final int CUSTOMER = 3;

    private UserRole() {
    }

    public static boolean isValid(int roleId) {
        return roleId == ADMIN || roleId == MANAGER || roleId == CUSTOMER;
    }

    public static boolean canAccessAdmin(int roleId) {
        return roleId == ADMIN || roleId == MANAGER;
    }

    public static String label(int roleId) {
        return switch (roleId) {
            case ADMIN -> "ADMIN";
            case MANAGER -> "MANAGER";
            default -> "CUSTOMER";
        };
    }
}
