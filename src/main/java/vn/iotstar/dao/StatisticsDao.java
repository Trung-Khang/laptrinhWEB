package vn.iotstar.dao;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import vn.iotstar.config.JpaConfig;

public class StatisticsDao {
    public long totalProducts() { return number("SELECT COUNT(*) FROM products").longValue(); }
    public long totalStock() { return number("SELECT COALESCE(SUM(stock_quantity),0) FROM products").longValue(); }
    public long lowStock() { return number("SELECT COUNT(*) FROM products WHERE stock_quantity BETWEEN 1 AND 5").longValue(); }
    public long outOfStock() { return number("SELECT COUNT(*) FROM products WHERE stock_quantity = 0").longValue(); }
    public BigDecimal inventoryValue() { return new BigDecimal(number("SELECT COALESCE(SUM(price * stock_quantity),0) FROM products").toString()); }

    @SuppressWarnings("unchecked")
    public List<Object[]> productByCategory() {
        return list("SELECT c.cate_name, COUNT(p.id) FROM Category c LEFT JOIN products p ON p.category_id = c.cate_id GROUP BY c.cate_name ORDER BY c.cate_name");
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> lowStockProducts() {
        return list("SELECT TOP 10 name, stock_quantity FROM products WHERE stock_quantity BETWEEN 0 AND 5 ORDER BY stock_quantity ASC, name ASC");
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> topSellingProducts() {
        return list("SELECT TOP 5 p.name, COALESCE(SUM(oi.quantity),0) AS sold FROM products p LEFT JOIN order_items oi ON oi.product_id = p.id GROUP BY p.name ORDER BY sold DESC, p.name ASC");
    }

    private Number number(String sql) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return (Number) em.createNativeQuery(sql).getSingleResult();
        } finally {
            em.close();
        }
    }

    private List<Object[]> list(String sql) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            Query query = em.createNativeQuery(sql);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
