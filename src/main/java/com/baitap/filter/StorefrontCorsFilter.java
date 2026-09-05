package com.baitap.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Allows the Vite development server to use the same Tomcat session cookie. */
@WebFilter("/api/*")
public class StorefrontCorsFilter implements Filter {
    private static final String VITE_ORIGIN = "http://localhost:5173";

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (VITE_ORIGIN.equals(req.getHeader("Origin"))) {
            resp.setHeader("Access-Control-Allow-Origin", VITE_ORIGIN);
            resp.setHeader("Access-Control-Allow-Credentials", "true");
            resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
            resp.setHeader("Vary", "Origin");
        }
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) { resp.setStatus(HttpServletResponse.SC_NO_CONTENT); return; }
        chain.doFilter(request, response);
    }
}
