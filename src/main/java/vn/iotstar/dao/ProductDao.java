package vn.iotstar.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.Product;

public class ProductDao implements IProductDao {

    @Override
    public void insert(Product product) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(product);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Product product) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(product);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Product product = em.find(Product.class, id);
            if (product != null) em.remove(product);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Product findById(int id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(
                "SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id", Product.class);
            query.setParameter("id", id);
            List<Product> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findAll() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createNamedQuery("Product.findAll", Product.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> search(String keyword, Integer categoryId, Boolean active, int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(buildSearchJpql(false), Product.class);
            bind(query, keyword, categoryId, active);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long count(String keyword, Integer categoryId, Boolean active) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(buildSearchJpql(true), Long.class);
            bind(query, keyword, categoryId, active);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean hasOrderItems(int productId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            Query tableCheck = em.createNativeQuery(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'order_items'");
            Number tables = (Number) tableCheck.getSingleResult();
            if (tables.longValue() == 0) return false;

            Query query = em.createNativeQuery("SELECT COUNT(*) FROM order_items WHERE product_id = ?");
            query.setParameter(1, productId);
            Number count = (Number) query.getSingleResult();
            return count.longValue() > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public void deactivate(int productId) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createQuery("UPDATE Product p SET p.active = false WHERE p.id = :id")
                    .setParameter("id", productId)
                    .executeUpdate();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private String buildSearchJpql(boolean count) {
        String select = count ? "SELECT COUNT(p)" : "SELECT p";
        String fetch = count ? "" : " JOIN FETCH p.category";
        return select + " FROM Product p" + fetch
            + " WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))"
            + " OR LOWER(COALESCE(p.description, '')) LIKE LOWER(CONCAT('%', :keyword, '%')))"
            + " AND (:categoryId IS NULL OR p.category.id = :categoryId)"
            + " AND (:active IS NULL OR p.active = :active)"
            + (count ? "" : " ORDER BY p.createdAt DESC, p.id DESC");
    }

    private void bind(Query query, String keyword, Integer categoryId, Boolean active) {
        query.setParameter("keyword", isBlank(keyword) ? null : keyword.trim());
        query.setParameter("categoryId", categoryId);
        query.setParameter("active", active);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
