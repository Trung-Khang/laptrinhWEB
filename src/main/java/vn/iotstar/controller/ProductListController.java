package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;

@WebServlet(urlPatterns = "/admin/product/list")
public class ProductListController extends ProductBaseController {
    private static final long serialVersionUID = 1L;
    private final IProductService productService = new ProductServiceImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String keyword = trim(req.getParameter("keyword"));
        Integer categoryId = parseInteger(req.getParameter("categoryId"));
        Boolean active = parseActive(req.getParameter("active"));
        int page = parseInteger(req.getParameter("page")) == null ? 1 : parseInteger(req.getParameter("page"));

        List<Product> products = productService.search(keyword, categoryId, active, page, PAGE_SIZE);
        long totalItems = productService.count(keyword, categoryId, active);
        int totalPages = (int) Math.ceil(totalItems * 1.0 / PAGE_SIZE);
        List<Category> categories = categoryService.findAll();

        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.getRequestDispatcher("/views/admin/list-product.jsp").forward(req, resp);
    }
}
