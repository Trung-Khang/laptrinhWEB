package vn.iotstar.service.impl;

import java.io.File;
import java.util.List;

import vn.iotstar.dao.CategoryDao;
import vn.iotstar.dao.ICategoryDao;
import vn.iotstar.entity.Category;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.util.Constant;

/**
 * Service implementation cho Category, delegate xuống JPA DAO (CategoryDao).
 * Giữ nguyên hành vi nghiệp vụ việc dọn file ảnh cũ khi cập nhật icon mới.
 */
public class CategoryServiceImpl implements ICategoryService {

    private ICategoryDao categoryDao = new CategoryDao();

    @Override
    public void insert(Category category) {
        categoryDao.insert(category);
    }

    @Override
    public void update(Category category) {
        // 1. Lấy category cũ từ database
        Category oldCategory = categoryDao.findById(category.getId());

        if (oldCategory != null) {
            // 2. Nếu category mới có icon mới (khác rỗng) thì xóa file ảnh cũ trên đĩa
            String newIcon = category.getIcon();
            if (newIcon != null && !newIcon.isEmpty()) {
                String oldIcon = oldCategory.getIcon();
                if (oldIcon != null && !oldIcon.isEmpty()) {
                    File oldFile = new File(Constant.DIR + "/" + oldIcon);
                    if (oldFile.exists()) {
                        oldFile.delete(); // Xóa file ảnh cũ
                    }
                }
            }
        }

        // 3. Cập nhật dữ liệu mới vào database
        categoryDao.update(category);
    }

    @Override
    public void delete(int categoryId) {
        categoryDao.delete(categoryId);
    }

    @Override
    public Category findById(int categoryId) {
        return categoryDao.findById(categoryId);
    }

    @Override
    public Category findByCategoryname(String name) {
        return categoryDao.findByCategoryname(name);
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public List<Category> searchByName(String name) {
        return categoryDao.searchByName(name);
    }

    @Override
    public List<Category> findAll(int page, int pageSize) {
        return categoryDao.findAll(page, pageSize);
    }

    @Override
    public long count() {
        return categoryDao.count();
    }
}
