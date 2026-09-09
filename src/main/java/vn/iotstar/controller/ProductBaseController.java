package vn.iotstar.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.validation.FormValidation;
import vn.iotstar.validation.ImageUploadUtil;

abstract class ProductBaseController extends HttpServlet {
    protected static final int PAGE_SIZE = 6;

    protected Map<String, String> validateProductForm(HttpServletRequest req, ICategoryService categoryService)
            throws IOException, ServletException {
        Map<String, String> errors = new LinkedHashMap<>();
        String name = FormValidation.trim(req.getParameter("name"));
        String description = FormValidation.trim(req.getParameter("description"));
        String categoryIdValue = FormValidation.trim(req.getParameter("categoryId"));
        String active = FormValidation.trim(req.getParameter("active"));

        FormValidation.required(errors, "name", name, "tên sản phẩm");
        FormValidation.maxLength(errors, "name", name, 255, "Tên sản phẩm");
        FormValidation.maxLength(errors, "description", description, 5000, "Mô tả");
        BigDecimal price = FormValidation.positiveDecimal(errors, "price", req.getParameter("price"), "Giá sản phẩm");
        Integer stock = FormValidation.nonNegativeInteger(errors, "stockQuantity", req.getParameter("stockQuantity"), "Tồn kho");

        Integer categoryId = parseInteger(categoryIdValue);
        if (categoryId == null) {
            errors.put("categoryId", "Vui lòng chọn danh mục.");
        } else if (categoryService.findById(categoryId) == null) {
            errors.put("categoryId", "Danh mục không tồn tại.");
        }
        if (!active.isEmpty() && !"true".equals(active) && !"false".equals(active)) {
            errors.put("active", "Trạng thái không hợp lệ.");
        }

        FormValidation.validateImage(errors, "imageFile", req.getPart("imageFile"), FormValidation.DEFAULT_IMAGE_MAX_BYTES);
        FormValidation.validateImageUrl(errors, "imageUrl", req.getParameter("imageUrl"));
        if (price != null && stock != null) {
            // Keep parsed values available to callers without duplicating parsing rules.
            req.setAttribute("validatedPrice", price);
            req.setAttribute("validatedStock", stock);
        }
        return errors;
    }

    protected Product readProduct(HttpServletRequest req, Product product) throws IOException, ServletException {
        if (product == null) product = new Product();
        product.setName(FormValidation.trim(req.getParameter("name")));
        product.setDescription(FormValidation.trim(req.getParameter("description")));
        product.setPrice((BigDecimal) req.getAttribute("validatedPrice"));
        product.setStockQuantity((Integer) req.getAttribute("validatedStock"));
        product.setActive(!"false".equals(req.getParameter("active")));

        Category category = new Category();
        category.setId(parseInteger(req.getParameter("categoryId")));
        product.setCategory(category);
        String imageUrl = FormValidation.trim(req.getParameter("imageUrl"));
        if (!imageUrl.isEmpty() && !hasUploadedImage(req)) product.setImage(imageUrl);
        return product;
    }

    protected void applyUploadedImage(HttpServletRequest req, Product product) throws IOException, ServletException {
        Part part = req.getPart("imageFile");
        if (hasUploadedImage(req)) product.setImage(ImageUploadUtil.save(part, "product"));
    }

    protected boolean hasUploadedImage(HttpServletRequest req) throws IOException, ServletException {
        Part part = req.getPart("imageFile");
        return part != null && part.getSize() > 0;
    }

    protected void setProductFormValues(HttpServletRequest req) {
        req.setAttribute("formSubmitted", true);
        req.setAttribute("formName", req.getParameter("name"));
        req.setAttribute("formDescription", req.getParameter("description"));
        req.setAttribute("formCategoryId", req.getParameter("categoryId"));
        req.setAttribute("formPrice", req.getParameter("price"));
        req.setAttribute("formStockQuantity", req.getParameter("stockQuantity"));
        req.setAttribute("formActive", req.getParameter("active"));
        req.setAttribute("formImageUrl", req.getParameter("imageUrl"));
    }

    protected Integer parseInteger(String value) {
        try {
            return value == null || value.trim().isEmpty() ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected String trim(String value) {
        return FormValidation.trim(value);
    }

    protected Boolean parseActive(String value) {
        String normalized = trim(value);
        if (normalized.isEmpty()) return null;
        return Boolean.valueOf(normalized);
    }
}
