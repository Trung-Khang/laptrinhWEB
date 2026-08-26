package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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
import vn.iotstar.util.Constant;

/**
 * Sửa danh mục (hỗ trợ đổi ảnh icon).
 * GET  /admin/category/edit?id=..  -> form sửa
 * POST /admin/category/edit        -> xử lý multipart (Jakarta Part) + cập nhật DB
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 MB
        maxFileSize = 1024 * 1024 * 5,        // 5 MB
        maxRequestSize = 1024 * 1024 * 10)    // 10 MB
@WebServlet(urlPatterns = "/admin/category/edit")
public class CategoryEditController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            int id = Integer.parseInt(idParam);
            Category category = categoryService.findById(id);
            req.setAttribute("category", category);
        }
        req.getRequestDispatcher("/views/admin/edit-category.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Native Jakarta multipart: đọc trường text và file qua Part API
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        Part part = req.getPart("icon");
        String newIcon = null; // null => giữ icon cũ

        if (part != null && part.getSize() > 0) {
            String fileName = Paths
                    .get(part.getSubmittedFileName())
                    .getFileName()
                    .toString();

            // Tạo tên file duy nhất bằng System.currentTimeMillis()
            String newFileName = System.currentTimeMillis() + "_"
                    + fileName.replaceAll("\\s+", "_");

            String dirPath = Constant.DIR + "/category";
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Ghi file vật lý vào đĩa
            part.write(dirPath + "/" + newFileName);

            newIcon = "category/" + newFileName;
        }

        // Lấy category cũ, cập nhật name và icon (nếu có icon mới)
        Category category = null;
        if (id != null && !id.trim().isEmpty()) {
            category = categoryService.findById(Integer.parseInt(id));
        }
        if (category != null) {
            category.setName(name);
            if (newIcon != null) {
                category.setIcon(newIcon);
            }
            // Service.update() sẽ tự xóa file ảnh cũ nếu có icon mới
            categoryService.update(category);
        }

        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }
}