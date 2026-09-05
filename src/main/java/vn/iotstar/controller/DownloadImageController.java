package vn.iotstar.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.util.Constant;

@WebServlet(urlPatterns = "/image")
public class DownloadImageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fname = req.getParameter("fname");
        if (fname == null || fname.isBlank()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Path root = Path.of(Constant.DIR).toAbsolutePath().normalize();
        Path file = root.resolve(fname.replace('\\', '/')).normalize();
        if (!file.startsWith(root) || !Files.isRegularFile(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = Files.probeContentType(file);
        resp.setContentType(mime != null ? mime : "application/octet-stream");
        resp.setHeader("Cache-Control", "public, max-age=3600");
        try (InputStream is = Files.newInputStream(file); OutputStream os = resp.getOutputStream()) {
            IOUtils.copy(is, os);
        }
    }
}
