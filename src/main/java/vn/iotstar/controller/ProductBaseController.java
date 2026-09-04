package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;
import vn.iotstar.util.Constant;

abstract class ProductBaseController extends HttpServlet {
    protected static final int PAGE_SIZE = 10;

    protected Product readProduct(HttpServletRequest req, Product product) throws IOException, jakarta.servlet.ServletException {
        if (product == null) product = new Product();
        product.setName(trim(req.getParameter("name")));
        product.setDescription(trim(req.getParameter("description")));
        product.setPrice(parseBigDecimal(req.getParameter("price")));
        product.setStockQuantity(parseInteger(req.getParameter("stockQuantity")));
        product.setActive("true".equals(req.getParameter("active")));

        Category category = new Category();
        category.setId(parseInteger(req.getParameter("categoryId")));
        product.setCategory(category);

        String imageUrl = trim(req.getParameter("imageUrl"));
        String uploadedImage = uploadImage(req.getPart("imageFile"));
        if (!isBlank(uploadedImage)) {
            product.setImage(uploadedImage);
        } else if (!isBlank(imageUrl)) {
            product.setImage(imageUrl);
        }
        return product;
    }

    protected String uploadImage(Part part) throws IOException {
        if (part == null || part.getSize() <= 0 || isBlank(part.getSubmittedFileName())) return null;
        String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String newFileName = System.currentTimeMillis() + "_" + fileName.replaceAll("\\s+", "_");
        String dirPath = Constant.DIR + "/product";
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();
        part.write(dirPath + "/" + newFileName);
        return "product/" + newFileName;
    }

    protected Integer parseInteger(String value) {
        try {
            return isBlank(value) ? null : Integer.valueOf(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected BigDecimal parseBigDecimal(String value) {
        try {
            return isBlank(value) ? null : new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected Boolean parseActive(String value) {
        if (isBlank(value)) return null;
        return Boolean.valueOf(value);
    }

    protected String trim(String value) {
        return value == null ? null : value.trim();
    }

    protected boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
