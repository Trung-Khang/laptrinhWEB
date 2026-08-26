package vn.iotstar.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.util.Constant;

/**
 * Servlet phục vụ hình ảnh: /image?fname=category/abc.jpg
 * Đọc file từ thư mục Constant.DIR và trả về cho trình duyệt.
 */
@WebServlet(urlPatterns = "/image")
public class DownloadImageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fname = req.getParameter("fname");

        // Chặn path traversal đơn giản (vd: ../, ..\)
        if (fname == null || fname.contains("..")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = new File(Constant.DIR + "/" + fname);
        resp.setContentType("image/jpeg");

        try (InputStream is = new FileInputStream(file);
             OutputStream os = resp.getOutputStream()) {
            // Dùng IOUtils.copy của Apache Commons IO để ghi ảnh ra response
            IOUtils.copy(is, os);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
