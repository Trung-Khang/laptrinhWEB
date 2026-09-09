package vn.iotstar.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.impl.CategoryServiceImpl;
import vn.iotstar.validation.FormValidation;
import vn.iotstar.validation.ImageUploadUtil;
import vn.iotstar.validation.ValidationException;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
@WebServlet(urlPatterns = "/admin/category/add")
public class CategoryAddController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setPageAttributes(req, "Thêm danh mục | KhangGear");
        req.getRequestDispatcher("/views/admin/add-category.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = FormValidation.trim(req.getParameter("name"));
        Part part = req.getPart("icon");
        Map<String, String> errors = new LinkedHashMap<>();
        FormValidation.required(errors, "name", name, "tên danh mục");
        FormValidation.maxLength(errors, "name", name, 150, "Tên danh mục");
        if (!name.isEmpty() && categoryService.findByCategoryname(name) != null) {
            errors.put("name", "Tên danh mục đã tồn tại.");
        }
        FormValidation.validateImage(errors, "icon", part, FormValidation.DEFAULT_IMAGE_MAX_BYTES);

        try {
            if (!errors.isEmpty()) throw new ValidationException(errors);
            Category category = new Category();
            category.setName(name);
            category.setIcon(part == null || part.getSize() == 0 ? "" : ImageUploadUtil.save(part, "category"));
            categoryService.insert(category);
            resp.sendRedirect(req.getContextPath() + "/admin/category/list?message="
                    + URLEncoder.encode("Thêm danh mục thành công.", StandardCharsets.UTF_8));
        } catch (ValidationException e) {
            req.setAttribute("errors", e.getErrors());
            req.setAttribute("formName", name);
            req.setAttribute("error", "Vui lòng kiểm tra lại các trường được đánh dấu.");
            setPageAttributes(req, "Thêm danh mục | KhangGear");
            req.getRequestDispatcher("/views/admin/add-category.jsp").forward(req, resp);
        }
    }

    private void setPageAttributes(HttpServletRequest req, String title) {
        req.setAttribute("pageTitle", title);
        req.setAttribute("activeMenu", "category");
    }
}
