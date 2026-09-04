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
            throw new IllegalStateException("Không thể xóa sản phẩm đã phát sinh đơn hàng.");
        }
        productDao.delete(id);
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
        if (product == null) throw new IllegalArgumentException("Dữ liệu sản phẩm không hợp lệ.");
        if (isBlank(product.getName())) throw new IllegalArgumentException("Vui lòng nhập tên sản phẩm.");
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            throw new IllegalArgumentException("Vui lòng chọn danh mục.");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá sản phẩm không được âm.");
        }
        if (product.getStockQuantity() == null || product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Tồn kho không được âm.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
