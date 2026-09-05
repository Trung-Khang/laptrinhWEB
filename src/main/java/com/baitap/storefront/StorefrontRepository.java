package com.baitap.storefront;

import com.baitap.api.dto.StorefrontDtos.CheckoutRequest;
import com.baitap.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.Category;
import vn.iotstar.entity.Order;
import vn.iotstar.entity.OrderItem;
import vn.iotstar.entity.Product;

/** Queries and transactional writes used only by the customer-facing API. */
public class StorefrontRepository {
    public List<Category> categories() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c ORDER BY c.name", Category.class).getResultList();
        } finally { em.close(); }
    }

    public Map<Integer, Long> productCountsByCategory() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Object[]> rows = em.createQuery("SELECT p.category.id, COUNT(p) FROM Product p WHERE p.active = true GROUP BY p.category.id", Object[].class).getResultList();
            Map<Integer, Long> result = new LinkedHashMap<>();
            for (Object[] row : rows) result.put((Integer) row[0], (Long) row[1]);
            return result;
        } finally { em.close(); }
    }

    public List<Product> products(String keyword, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice,
                                  boolean inStock, String sort, int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(productJpql(false, sort, inStock), Product.class);
            bindProducts(query, keyword, categoryId, minPrice, maxPrice, inStock);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally { em.close(); }
    }

    public long countProducts(String keyword, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, boolean inStock) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(productJpql(true, "newest", inStock), Long.class);
            bindProducts(query, keyword, categoryId, minPrice, maxPrice, inStock);
            return query.getSingleResult();
        } finally { em.close(); }
    }

    public Product productById(int id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Product> products = em.createQuery("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id AND p.active = true", Product.class)
                    .setParameter("id", id).getResultList();
            return products.isEmpty() ? null : products.get(0);
        } finally { em.close(); }
    }

    public List<Product> featured(int limit) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Product p JOIN FETCH p.category WHERE p.active = true AND p.stockQuantity > 0 ORDER BY p.createdAt DESC, p.id DESC", Product.class)
                    .setMaxResults(limit).getResultList();
        } finally { em.close(); }
    }

    public List<Product> bestSelling(int limit) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            @SuppressWarnings("unchecked")
            List<Number> productIds = em.createNativeQuery(
                    "SELECT TOP (" + Math.max(1, limit) + ") oi.product_id "
                            + "FROM dbo.order_items oi INNER JOIN dbo.products p ON p.id = oi.product_id "
                            + "WHERE p.active = 1 GROUP BY oi.product_id ORDER BY SUM(oi.quantity) DESC")
                    .getResultList();
            if (productIds.isEmpty()) return featured(limit);
            List<Integer> ids = productIds.stream().map(Number::intValue).toList();
            List<Product> products = em.createQuery("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id IN :ids", Product.class)
                    .setParameter("ids", ids).getResultList();
            Map<Integer, Product> byId = new LinkedHashMap<>();
            for (Product product : products) byId.put(product.getId(), product);
            return ids.stream().map(byId::get).filter(product -> product != null).toList();
        } finally { em.close(); }
    }

    public List<Product> relatedProducts(int categoryId, int productId, int limit) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("SELECT p FROM Product p JOIN FETCH p.category WHERE p.active = true AND p.category.id = :categoryId AND p.id <> :productId ORDER BY p.createdAt DESC", Product.class)
                    .setParameter("categoryId", categoryId).setParameter("productId", productId).setMaxResults(limit).getResultList();
        } finally { em.close(); }
    }

    public Map<Integer, Product> productsByIds(Collection<Integer> productIds) {
        if (productIds == null || productIds.isEmpty()) return Map.of();
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Product> products = em.createQuery("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id IN :ids AND p.active = true", Product.class)
                    .setParameter("ids", productIds).getResultList();
            Map<Integer, Product> result = new LinkedHashMap<>();
            for (Product product : products) result.put(product.getId(), product);
            return result;
        } finally { em.close(); }
    }

    public Order checkout(User user, CheckoutRequest request, Map<Integer, Integer> cart) {
        if (cart == null || cart.isEmpty()) throw new IllegalArgumentException("Gio hang dang trong.");
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Order order = new Order();
            order.setUserId(user.getId());
            order.setCustomerName(request.fullName());
            order.setPhone(request.phone());
            order.setEmail(request.email());
            order.setShippingAddress(request.address());
            order.setNote(request.note());
            order.setPaymentMethod(request.paymentMethod());
            order.setPaymentStatus("PENDING");
            order.setStatus("PENDING");
            BigDecimal total = BigDecimal.ZERO;
            List<OrderItem> items = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
                int quantity = entry.getValue() == null ? 0 : entry.getValue();
                Product product = em.find(Product.class, entry.getKey(), LockModeType.PESSIMISTIC_WRITE);
                if (product == null || !Boolean.TRUE.equals(product.getActive())) throw new IllegalArgumentException("San pham trong gio hang khong con ban.");
                if (quantity < 1 || product.getStockQuantity() < quantity) throw new IllegalArgumentException("San pham '" + product.getName() + "' khong du ton kho.");
                BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(quantity));
                product.setStockQuantity(product.getStockQuantity() - quantity);
                OrderItem item = new OrderItem();
                item.setOrder(order); item.setProduct(product); item.setQuantity(quantity);
                item.setUnitPrice(product.getPrice()); item.setSubtotal(subtotal);
                items.add(item); total = total.add(subtotal);
            }
            order.setTotalAmount(total);
            order.setItems(items);
            em.persist(order);
            tx.commit();
            return order;
        } catch (RuntimeException exception) {
            if (tx.isActive()) tx.rollback();
            throw exception;
        } finally { em.close(); }
    }

    public List<Order> ordersForUser(int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createQuery("SELECT DISTINCT o FROM StoreOrder o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.category WHERE o.userId = :userId ORDER BY o.orderDate DESC", Order.class)
                    .setParameter("userId", userId).getResultList();
        } finally { em.close(); }
    }

    public Order orderForUser(int orderId, int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            List<Order> orders = em.createQuery("SELECT DISTINCT o FROM StoreOrder o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.category WHERE o.id = :id AND o.userId = :userId", Order.class)
                    .setParameter("id", orderId).setParameter("userId", userId).getResultList();
            return orders.isEmpty() ? null : orders.get(0);
        } finally { em.close(); }
    }

    public Order cancelOrder(int orderId, int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<Order> orders = em.createQuery("SELECT DISTINCT o FROM StoreOrder o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product WHERE o.id = :id AND o.userId = :userId", Order.class)
                    .setParameter("id", orderId).setParameter("userId", userId).getResultList();
            if (orders.isEmpty()) throw new IllegalArgumentException("Khong tim thay don hang.");
            Order order = orders.get(0);
            if (!"PENDING".equals(order.getStatus())) throw new IllegalStateException("Don hang chi co the huy khi dang cho xac nhan.");
            for (OrderItem item : order.getItems()) {
                Product product = em.find(Product.class, item.getProduct().getId(), LockModeType.PESSIMISTIC_WRITE);
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            }
            order.setStatus("CANCELLED");
            tx.commit();
            return order;
        } catch (RuntimeException exception) {
            if (tx.isActive()) tx.rollback();
            throw exception;
        } finally { em.close(); }
    }

    private String productJpql(boolean count, String sort, boolean inStock) {
        String select = count ? "SELECT COUNT(p)" : "SELECT p";
        String fetch = count ? "" : " JOIN FETCH p.category";
        String order = "";
        String normalizedSort = sort == null ? "newest" : sort;
        if (!count) {
            order = switch (normalizedSort) {
                case "price-asc" -> " ORDER BY p.price ASC, p.id DESC";
                case "price-desc" -> " ORDER BY p.price DESC, p.id DESC";
                default -> " ORDER BY p.createdAt DESC, p.id DESC";
            };
        }
        return select + " FROM Product p" + fetch + " WHERE p.active = true"
                + " AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))"
                + " AND (:categoryId IS NULL OR p.category.id = :categoryId)"
                + " AND (:minPrice IS NULL OR p.price >= :minPrice)"
                + " AND (:maxPrice IS NULL OR p.price <= :maxPrice)"
                + (inStock ? " AND p.stockQuantity > 0" : "") + order;
    }

    private void bindProducts(Query query, String keyword, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, boolean inStock) {
        query.setParameter("keyword", keyword == null || keyword.isBlank() ? null : keyword.trim());
        query.setParameter("categoryId", categoryId);
        query.setParameter("minPrice", minPrice);
        query.setParameter("maxPrice", maxPrice);
    }
}
