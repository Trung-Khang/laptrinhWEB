package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.entity.Product;

public interface IProductDao {
    void insert(Product product);
    void update(Product product);
    void delete(int id);
    Product findById(int id);
    List<Product> findAll();
    List<Product> search(String keyword, Integer categoryId, Boolean active, int page, int pageSize);
    long count(String keyword, Integer categoryId, Boolean active);
    boolean hasOrderItems(int productId);
}
