package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.entity.Category;

/**
 * DAO interface cho Category - sử dụng JPA/Hibernate (EntityManager).
 * Các thao tác CRUD + truy vấn theo yêu cầu assignment JPA.
 */
public interface ICategoryDao {

    void insert(Category category);

    void update(Category category);

    void delete(int categoryId);

    Category findById(int categoryId);

    Category findByCategoryname(String name);

    List<Category> findAll();

    List<Category> searchByName(String name);

    List<Category> findAll(int page, int pageSize);

    long count();
}
