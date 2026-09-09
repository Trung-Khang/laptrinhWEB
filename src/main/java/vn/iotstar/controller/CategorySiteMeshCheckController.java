package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** Diagnostics route for verifying the isolated Category decorator. */
@WebServlet(urlPatterns = "/admin/category/sitemesh-check")
public class CategorySiteMeshCheckController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("pageTitle", "SiteMesh check | KhangGear");
        request.setAttribute("activeMenu", "category");
        getServletContext().getRequestDispatcher("/views/admin/sitemesh-check.jsp").include(request, response);
    }
}
