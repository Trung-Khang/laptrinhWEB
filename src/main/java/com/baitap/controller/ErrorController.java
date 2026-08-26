package com.baitap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/error"})
public class ErrorController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processError(req, resp);
    }

    private void processError(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object statusObj = req.getAttribute("javax.servlet.error.status_code");

        if (statusObj != null) {
            int statusCode = Integer.parseInt(statusObj.toString());

            if (statusCode == 404) {
                req.getRequestDispatcher("/WEB-INF/views/loi404.jsp").forward(req, resp);
                return;
            }

            if (statusCode == 500) {
                req.getRequestDispatcher("/WEB-INF/views/loi500.jsp").forward(req, resp);
                return;
            }
        }

        // Trường hợp mặc định (vd: đăng nhập thất bại chuyển hướng tới /error)
        req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
    }
}