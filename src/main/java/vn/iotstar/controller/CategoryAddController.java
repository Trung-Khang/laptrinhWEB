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
 * Thêm mới danh mục (hỗ trợ upload ảnh icon).
 * GET  /admin/category/add -> form thêm mới
 * POST /admin/category/add -> xử lý multipart (Jakarta Part API) + lưu file + insert DB
 */
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,      // 1 MB
        maxFileSize = 1024 * 1024 * 5,        // 5 MB
        maxRequestSize = 1024 * 1024 * 10)    // 10 MB
@WebServlet(urlPatterns = "/admin/category/add")
public class CategoryAddController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("pageTitle", "Thêm danh mục | KhangGear");
        req.setAttribute("activeMenu", "category");
        req.getRequestDispatcher("/views/admin/add-category.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Native Jakarta multipart: đọc trường text và file qua Part API
        String name = req.getParameter("name");
        Part part = req.getPart("icon");
        String icon = "";

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

            // Lưu đường dẫn tương đối để hiển thị qua /image
            icon = "category/" + newFileName;
        }

        Category category = new Category();
        category.setName(name);
        category.setIcon(icon);
        categoryService.insert(category);

        resp.sendRedirect(req.getContextPath() + "/admin/category/list");
    }
}
