package vn.iotstar.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.Order;

public class OrderDao implements IOrderDao {
    @Override
    public List<Order> search(String keyword, String status, int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Order> query = em.createQuery(buildJpql(false), Order.class);
            bind(query, keyword, status);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long count(String keyword, String status) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(buildJpql(true), Long.class);
            bind(query, keyword, status);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public Order findById(int id) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Order> query = em.createQuery(
                "SELECT DISTINCT o FROM StoreOrder o LEFT JOIN FETCH o.items i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.category WHERE o.id = :id",
                Order.class);
            query.setParameter("id", id);
            List<Order> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public void updateStatus(int id, String status) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Query query = em.createQuery("UPDATE StoreOrder o SET o.status = :status WHERE o.id = :id");
            query.setParameter("status", status);
            query.setParameter("id", id);
            query.executeUpdate();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private String buildJpql(boolean count) {
        String select = count ? "SELECT COUNT(o)" : "SELECT o";
        return select + " FROM StoreOrder o"
            + " WHERE (:keyword IS NULL OR CAST(o.id AS string) LIKE CONCAT('%', :keyword, '%')"
            + " OR LOWER(o.customerName) LIKE LOWER(CONCAT('%', :keyword, '%'))"
            + " OR o.phone LIKE CONCAT('%', :keyword, '%'))"
            + " AND (:status IS NULL OR o.status = :status)"
            + (count ? "" : " ORDER BY o.orderDate DESC, o.id DESC");
    }

    private void bind(Query query, String keyword, String status) {
        query.setParameter("keyword", isBlank(keyword) ? null : keyword.trim());
        query.setParameter("status", isBlank(status) ? null : status.trim());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
