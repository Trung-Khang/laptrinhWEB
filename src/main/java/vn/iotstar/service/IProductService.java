package vn.iotstar.service;

import java.util.List;

import vn.iotstar.entity.Product;

public interface IProductService {
    void insert(Product product);
    void update(Product product);
    void delete(int id);
    boolean hasOrderItems(int id);
    Product findById(int id);
    List<Product> search(String keyword, Integer categoryId, Boolean active, int page, int pageSize);
    long count(String keyword, Integer categoryId, Boolean active);
}
