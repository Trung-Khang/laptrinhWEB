package com.baitap.api;

import com.baitap.api.dto.StorefrontDtos.CategoryDto;
import com.baitap.api.dto.StorefrontDtos.ProductPageDto;
import com.baitap.storefront.StorefrontRepository;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Product;

@WebServlet(urlPatterns = {"/api/storefront/categories", "/api/storefront/products", "/api/storefront/products/*", "/api/storefront/products/latest"})
public class StorefrontCatalogApiController extends BaseApiServlet {
    private static final int MAX_PAGE_SIZE = 24;
    private final StorefrontRepository repository = new StorefrontRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String path = request.getRequestURI().substring(request.getContextPath().length());
            if (path.endsWith("/categories")) { categories(request, response); return; }
            if (path.endsWith("/products/latest")) { latest(request, response); return; }
            if (path.endsWith("/products/featured")) { ok(response, repository.featured(8).stream().map(product -> StorefrontMapper.product(product, request.getContextPath())).toList()); return; }
            if (path.endsWith("/products/best-selling")) { ok(response, repository.bestSelling(8).stream().map(product -> StorefrontMapper.product(product, request.getContextPath())).toList()); return; }
            String suffix = request.getPathInfo();
            if (suffix != null && suffix.length() > 1) {
                Product product = repository.productById(Integer.parseInt(suffix.substring(1)));
                if (product == null) { error(response, 404, "Khong tim thay san pham."); return; }
                ok(response, Map.of("product", StorefrontMapper.product(product, request.getContextPath()), "related",
                        repository.relatedProducts(product.getCategory().getId(), product.getId(), 4).stream().map(item -> StorefrontMapper.product(item, request.getContextPath())).toList()));
                return;
            }
            products(request, response);
        } catch (NumberFormatException exception) {
            error(response, 400, "Ma san pham khong hop le.");
        } catch (RuntimeException exception) {
            getServletContext().log("Storefront catalog API failed", exception);
            error(response, 500, "Khong the tai du lieu san pham luc nay.");
        }
    }

    private void categories(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<Integer, Long> counts = repository.productCountsByCategory();
        List<CategoryDto> result = repository.categories().stream()
                .map(category -> StorefrontMapper.category(category, counts.getOrDefault(category.getId(), 0L), request.getContextPath())).toList();
        ok(response, result);
    }

    private void products(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int page = Math.max(1, integer(request, "page", 1));
        int pageSize = Math.min(MAX_PAGE_SIZE, Math.max(1, integer(request, "size", integer(request, "pageSize", 12))));
        Integer categoryId = integer(request, "categoryId", 0); if (categoryId <= 0) categoryId = null;
        boolean inStock = Boolean.parseBoolean(request.getParameter("inStock"));
        BigDecimal minPrice = decimal(request, "minPrice"); BigDecimal maxPrice = decimal(request, "maxPrice");
        if (minPrice != null && minPrice.signum() < 0 || maxPrice != null && maxPrice.signum() < 0 || minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            error(response, 400, "Khoang gia khong hop le."); return;
        }
        String keyword = request.getParameter("q"); String sort = request.getParameter("sort");
        long total = repository.countProducts(keyword, categoryId, minPrice, maxPrice, inStock);
        int totalPages = Math.max(1, (int) Math.ceil((double) total / pageSize));
        if (page > totalPages) page = totalPages;
        List<Product> items = repository.products(keyword, categoryId, minPrice, maxPrice, inStock, sort, page, pageSize);
        ok(response, new ProductPageDto(items.stream().map(product -> StorefrontMapper.product(product, request.getContextPath())).toList(), page, pageSize, total, totalPages, page > 1, page < totalPages));
    }

    private void latest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int limit = Math.min(10, Math.max(1, integer(request, "limit", 10)));
        ok(response, repository.latest(limit).stream()
                .map(product -> StorefrontMapper.product(product, request.getContextPath())).toList());
    }
}
