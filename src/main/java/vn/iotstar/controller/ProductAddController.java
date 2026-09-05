package vn.iotstar.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Product;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.service.impl.ProductServiceImpl;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
@WebServlet(urlPatterns = "/admin/product/add")
public class ProductAddController extends ProductBaseController {
    private static final long serialVersionUID = 1L;
    private final IProductService productService = new ProductServiceImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", categoryService.findAll());
        req.getRequestDispatcher("/views/admin/add-product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            Product product = readProduct(req, new Product());
            productService.insert(product);
            resp.sendRedirect(req.getContextPath() + "/admin/product/list?message="
                    + URLEncoder.encode("Th\u00eam s\u1ea3n ph\u1ea9m th\u00e0nh c\u00f4ng.", StandardCharsets.UTF_8));
        } catch (Exception e) {
            getServletContext().log("Add product failed", e);
            req.setAttribute("error", e.getMessage());
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher("/views/admin/add-product.jsp").forward(req, resp);
        }
    }
}
