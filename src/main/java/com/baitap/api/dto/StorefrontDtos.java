package com.baitap.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/** JSON-only data contracts for the customer storefront. */
public final class StorefrontDtos {
    private StorefrontDtos() { }

    public record ApiResponse(boolean success, String message, Object data, Object fieldErrors) { }

    public record CategoryDto(Integer id, String name, String icon, String iconUrl, long productCount) { }

    public record ProductDto(Integer id, String name, CategoryDto category, String description,
                             BigDecimal price, Integer stockQuantity, String image,
                             LocalDateTime createdAt, long soldQuantity) { }

    public record ProductPageDto(List<ProductDto> items, int page, int pageSize,
                                 long totalItems, int totalPages, boolean hasPrevious, boolean hasNext) { }

    public record CartItemDto(ProductDto product, int quantity, BigDecimal subtotal) { }

    public record CartDto(List<CartItemDto> items, int itemCount, BigDecimal total) { }

    public record CheckoutRequest(String fullName, String phone, String email, String address,
                                  String note, String paymentMethod) { }

    public record UserProfileDto(int id, String username, String fullName, String email,
                                 String phone, String role, String avatar, String avatarUrl) { }

    public record OrderItemDto(Integer id, ProductDto product, int quantity,
                               BigDecimal unitPrice, BigDecimal subtotal) { }

    public record OrderDto(Integer id, String customerName, String phone, String email,
                           String shippingAddress, LocalDateTime orderDate, BigDecimal totalAmount,
                           String status, String note, String paymentMethod, String paymentStatus,
                           List<OrderItemDto> items, boolean cancellable) { }
}
