package vn.iotstar.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
        deleteAndRedirect(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        deleteAndRedirect(req, resp);
    }

    private void deleteAndRedirect(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String parameter = "message";
        String message = "Xóa danh mục thành công.";
        try {
            categoryService.delete(Integer.parseInt(req.getParameter("id")));
        } catch (NumberFormatException e) {
            parameter = "error";
            message = "Mã danh mục không hợp lệ.";
        } catch (RuntimeException e) {
            getServletContext().log("Không thể xóa danh mục đang được sử dụng", e);
            parameter = "error";
            message = "Không thể xóa danh mục vì vẫn còn sản phẩm thuộc danh mục này.";
        }
        resp.sendRedirect(req.getContextPath() + "/admin/category/list?" + parameter + "="
                + URLEncoder.encode(message, StandardCharsets.UTF_8));
    }
}
