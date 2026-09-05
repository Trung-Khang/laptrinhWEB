package vn.iotstar.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.impl.ProductServiceImpl;

@WebServlet(urlPatterns = "/admin/product/delete")
public class ProductDeleteController extends ProductBaseController {
    private static final long serialVersionUID = 1L;
    private final IProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/admin/product/list");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer id = parseInteger(req.getParameter("id"));
            if (id == null) throw new IllegalArgumentException("M\u00e3 s\u1ea3n ph\u1ea9m kh\u00f4ng h\u1ee3p l\u1ec7.");
            boolean archived = productService.hasOrderItems(id);
            productService.delete(id);
            redirect(req, resp, "message", archived
                    ? "S\u1ea3n ph\u1ea9m \u0111\u00e3 c\u00f3 trong l\u1ecbch s\u1eed \u0111\u01a1n h\u00e0ng n\u00ean \u0111\u01b0\u1ee3c chuy\u1ec3n sang Ng\u1eebng b\u00e1n."
                    : "X\u00f3a s\u1ea3n ph\u1ea9m th\u00e0nh c\u00f4ng.");
        } catch (Exception e) {
            getServletContext().log("Delete product failed", e);
            redirect(req, resp, "error", e.getMessage());
        }
    }

    private void redirect(HttpServletRequest req, HttpServletResponse resp, String key, String message) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/admin/product/list?" + key + "="
                + URLEncoder.encode(message, StandardCharsets.UTF_8));
    }
}
