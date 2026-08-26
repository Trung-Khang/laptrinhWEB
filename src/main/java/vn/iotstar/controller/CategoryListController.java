package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;

/**
 * Hiển thị danh sách danh mục.
 * GET/POST /admin/category/list (hỗ trợ tìm kiếm theo keyword).
 */
@WebServlet(urlPatterns = "/admin/category/list")
public class CategoryListController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        List<Category> cateList;

        // Nếu có từ khóa tìm kiếm thì gọi search, ngược lại lấy toàn bộ
        if (keyword != null && !keyword.trim().isEmpty()) {
            cateList = categoryService.searchByName(keyword.trim());
        } else {
            cateList = categoryService.findAll();
        }

        req.setAttribute("cateList", cateList);
        req.getRequestDispatcher("/views/admin/list-category.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
