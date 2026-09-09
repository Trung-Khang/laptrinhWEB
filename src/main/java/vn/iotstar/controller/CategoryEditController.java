package vn.iotstar.controller;

import java.io.IOException;
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
@WebServlet(urlPatterns = "/admin/category/edit")
public class CategoryEditController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category category = findCategory(req.getParameter("id"));
        req.setAttribute("category", category);
        setPageAttributes(req);
        req.getRequestDispatcher("/views/admin/edit-category.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Integer id = parseId(req.getParameter("id"));
        String name = FormValidation.trim(req.getParameter("name"));
        Part part = req.getPart("icon");
        Category category = id == null ? null : categoryService.findById(id);
        Map<String, String> errors = new LinkedHashMap<>();

        if (category == null) errors.put("id", "Danh mục không tồn tại.");
        FormValidation.required(errors, "name", name, "tên danh mục");
        FormValidation.maxLength(errors, "name", name, 150, "Tên danh mục");
        Category duplicate = name.isEmpty() ? null : categoryService.findByCategoryname(name);
        if (duplicate != null && (category == null || !duplicate.getId().equals(category.getId()))) {
            errors.put("name", "Tên danh mục đã tồn tại.");
        }
        FormValidation.validateImage(errors, "icon", part, FormValidation.DEFAULT_IMAGE_MAX_BYTES);

        try {
            if (!errors.isEmpty()) throw new ValidationException(errors);
            category.setName(name);
            if (part != null && part.getSize() > 0) category.setIcon(ImageUploadUtil.save(part, "category"));
            categoryService.update(category);
            resp.sendRedirect(req.getContextPath() + "/admin/category/list?message="
                    + java.net.URLEncoder.encode("Cập nhật danh mục thành công.", java.nio.charset.StandardCharsets.UTF_8));
        } catch (ValidationException e) {
            if (category == null) category = new Category();
            category.setName(name);
            req.setAttribute("category", category);
            req.setAttribute("errors", e.getErrors());
            req.setAttribute("formName", name);
            req.setAttribute("error", "Vui lòng kiểm tra lại các trường được đánh dấu.");
            setPageAttributes(req);
            req.getRequestDispatcher("/views/admin/edit-category.jsp").forward(req, resp);
        }
    }

    private Category findCategory(String value) {
        Integer id = parseId(value);
        return id == null ? null : categoryService.findById(id);
    }

    private Integer parseId(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void setPageAttributes(HttpServletRequest req) {
        req.setAttribute("pageTitle", "Sửa danh mục | KhangGear");
        req.setAttribute("activeMenu", "category");
    }
}
