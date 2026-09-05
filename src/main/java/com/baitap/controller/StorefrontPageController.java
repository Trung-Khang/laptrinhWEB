package com.baitap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/** BrowserRouter entry points for the production React build. */
@WebServlet(urlPatterns = {"/products", "/products/*", "/cart", "/checkout", "/account/*"})
public class StorefrontPageController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/storefront/index.html").forward(request, response);
    }
}
