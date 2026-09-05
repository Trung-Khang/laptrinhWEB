package vn.iotstar.service.impl;

import java.math.BigDecimal;
import java.util.List;

import vn.iotstar.dao.IProductDao;
import vn.iotstar.dao.ProductDao;
import vn.iotstar.entity.Product;
import vn.iotstar.service.IProductService;

public class ProductServiceImpl implements IProductService {
    private final IProductDao productDao = new ProductDao();

    @Override
    public void insert(Product product) {
        validate(product);
        productDao.insert(product);
    }

    @Override
    public void update(Product product) {
        validate(product);
        productDao.update(product);
    }

    @Override
    public void delete(int id) {
        if (productDao.hasOrderItems(id)) {
            productDao.deactivate(id);
            return;
        }
        productDao.delete(id);
    }

    @Override
    public boolean hasOrderItems(int id) {
        return productDao.hasOrderItems(id);
    }

    @Override
    public Product findById(int id) {
        return productDao.findById(id);
    }

    @Override
    public List<Product> search(String keyword, Integer categoryId, Boolean active, int page, int pageSize) {
        return productDao.search(keyword, categoryId, active, Math.max(page, 1), pageSize);
    }

    @Override
    public long count(String keyword, Integer categoryId, Boolean active) {
        return productDao.count(keyword, categoryId, active);
    }

    private void validate(Product product) {
        if (product == null) throw new IllegalArgumentException("D\u1eef li\u1ec7u s\u1ea3n ph\u1ea9m kh\u00f4ng h\u1ee3p l\u1ec7.");
        if (isBlank(product.getName())) throw new IllegalArgumentException("Vui l\u00f2ng nh\u1eadp t\u00ean s\u1ea3n ph\u1ea9m.");
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            throw new IllegalArgumentException("Vui l\u00f2ng ch\u1ecdn danh m\u1ee5c.");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Gi\u00e1 s\u1ea3n ph\u1ea9m kh\u00f4ng \u0111\u01b0\u1ee3c \u00e2m.");
        }
        if (product.getStockQuantity() == null || product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("T\u1ed3n kho kh\u00f4ng \u0111\u01b0\u1ee3c \u00e2m.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
