package vn.iotstar.dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.entity.Category;

/**
 * JPA DAO implementation cho Category.
 *
 * Dùng EntityManager + JPQL/NamedQuery thay vì raw JDBC SQL.
 * Mọi thao tác ghi (insert/update/delete) đều nằm trong transaction.
 */
public class CategoryDao implements ICategoryDao {

    @Override
    public void insert(Category category) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(category);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Category category) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(category);
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int categoryId) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Category category = em.find(Category.class, categoryId);
            if (category != null) {
                em.remove(category);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Category findById(int categoryId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.find(Category.class, categoryId);
        } finally {
            em.close();
        }
    }

    @Override
    public Category findByCategoryname(String name) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            if (name == null) {
                return null;
            }
            String jpql = "SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name)";
            TypedQuery<Category> query = em.createQuery(jpql, Category.class);
            query.setParameter("name", name);
            List<Category> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> findAll() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.createNamedQuery("Category.findAll", Category.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> searchByName(String name) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))";
            TypedQuery<Category> query = em.createQuery(jpql, Category.class);
            query.setParameter("keyword", name);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> findAll(int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Category> query = em.createNamedQuery("Category.findAll", Category.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Category c", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
