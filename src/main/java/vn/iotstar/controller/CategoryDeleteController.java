package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;

/**
 * Xóa danh mục theo id.
 * GET/POST /admin/category/delete?id=..
 */
@WebServlet(urlPatterns = "/admin/category/delete")
public class CategoryDeleteController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            categoryService.delete(id);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

