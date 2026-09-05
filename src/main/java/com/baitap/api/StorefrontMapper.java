package com.baitap.api;

import com.baitap.api.dto.StorefrontDtos.CategoryDto;
import com.baitap.api.dto.StorefrontDtos.OrderDto;
import com.baitap.api.dto.StorefrontDtos.OrderItemDto;
import com.baitap.api.dto.StorefrontDtos.ProductDto;
import com.baitap.api.dto.StorefrontDtos.UserProfileDto;
import com.baitap.model.User;
import java.math.BigDecimal;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.OrderItem;
import vn.iotstar.entity.Product;
import vn.iotstar.util.Constant;

final class StorefrontMapper {
    private StorefrontMapper() { }

    static CategoryDto category(Category category, long productCount, String contextPath) {
        return new CategoryDto(category.getId(), category.getName(), category.getIcon(),
                publicImageUrl(contextPath, category.getIcon()), productCount);
    }

    static ProductDto product(Product product) { return product(product, 0, ""); }

    static ProductDto product(Product product, String contextPath) { return product(product, 0, contextPath); }

    static ProductDto product(Product product, long soldQuantity, String contextPath) {
        if (product == null) return null;
        CategoryDto category = product.getCategory() == null ? null : category(product.getCategory(), 0, contextPath);
        return new ProductDto(product.getId(), product.getName(), category, product.getDescription(), product.getPrice(),
                product.getStockQuantity(), product.getImage(), product.getCreatedAt(), soldQuantity);
    }

    private static String publicImageUrl(String contextPath, String source) {
        if (source == null || source.isBlank()) return null;
        String trimmed = source.trim();
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) return trimmed;
        String normalized = trimmed.replace('\\', '/');
        while (normalized.startsWith("/")) normalized = normalized.substring(1);
        if (normalized.contains("..")) return null;
        String encoded = URLEncoder.encode(normalized, StandardCharsets.UTF_8).replace("+", "%20");
        File file = new File(Constant.DIR, normalized);
        String version = file.isFile() ? "&v=" + file.lastModified() : "";
        return contextPath + "/image?fname=" + encoded + version;
    }

    static UserProfileDto profile(User user) {
        return new UserProfileDto(user.getId(), user.getUserName(), user.getFullName(), user.getEmail(), user.getPhone(),
                com.baitap.model.UserRole.label(user.getRoleid()));
    }

    static OrderDto order(Order order) {
        List<OrderItemDto> items = order.getItems() == null ? List.of() : order.getItems().stream().map(StorefrontMapper::orderItem).toList();
        boolean cancellable = "PENDING".equals(order.getStatus());
        return new OrderDto(order.getId(), order.getCustomerName(), order.getPhone(), order.getEmail(), order.getShippingAddress(),
                order.getOrderDate(), order.getTotalAmount(), order.getStatus(), order.getNote(), order.getPaymentMethod(),
                order.getPaymentStatus(), items, cancellable);
    }

    private static OrderItemDto orderItem(OrderItem item) {
        return new OrderItemDto(item.getId(), product(item.getProduct()), item.getQuantity(), item.getUnitPrice(), item.getSubtotal());
    }
}
