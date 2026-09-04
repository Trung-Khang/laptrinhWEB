package com.baitap.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class FlashMessage {
    private FlashMessage() {
    }

    public static void success(HttpServletRequest request, String message) {
        request.getSession(true).setAttribute("flashSuccess", message);
    }

    public static void error(HttpServletRequest request, String message) {
        request.getSession(true).setAttribute("flashError", message);
    }

    public static void expose(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return;
        Object success = session.getAttribute("flashSuccess");
        Object error = session.getAttribute("flashError");
        if (success != null) { request.setAttribute("flashSuccess", success); session.removeAttribute("flashSuccess"); }
        if (error != null) { request.setAttribute("flashError", error); session.removeAttribute("flashError"); }
    }
}
