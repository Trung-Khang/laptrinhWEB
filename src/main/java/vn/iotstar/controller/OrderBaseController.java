package vn.iotstar.controller;

import jakarta.servlet.http.HttpServlet;

abstract class OrderBaseController extends HttpServlet {
    protected static final int PAGE_SIZE = 10;

    protected Integer parseInteger(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected String trim(String value) {
        return value == null ? null : value.trim();
    }
}
