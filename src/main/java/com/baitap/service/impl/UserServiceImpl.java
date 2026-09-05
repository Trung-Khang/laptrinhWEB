package com.baitap.service.impl;

import com.baitap.dao.UserDao;
import com.baitap.dao.impl.UserDaoImpl;
import com.baitap.model.User;
import com.baitap.model.UserRole;
import com.baitap.service.UserService;
import java.sql.Date;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao = new UserDaoImpl();

    @Override public User findByUsername(String username) { return userDao.findByUsername(username); }
    @Override public boolean authenticate(String username, String password) { User user = userDao.findByUsername(username); return user != null && user.isActive() && user.getPassword().equals(password); }
    @Override public boolean checkExistEmail(String email) { return userDao.checkExistEmail(email); }
    @Override public boolean checkExistUsername(String username) { return userDao.checkExistUsername(username); }
    @Override public boolean checkExistPhone(String phone) { return userDao.checkExistPhone(phone); }
    @Override public void insert(User user) { userDao.insert(user); }
    @Override public List<User> search(String keyword, Integer roleid, Boolean active) { return userDao.search(keyword, roleid, active); }
    @Override public User findById(int id) { return userDao.findById(id); }
    @Override public void update(User user) { userDao.update(user); }
    @Override
    public void updateProfile(User user) {
        User stored = userDao.findById(user.getId());
        if (stored == null) throw new IllegalArgumentException("Khong tim thay nguoi dung.");
        normalize(user);
        if (isBlank(user.getFullName())) throw new IllegalArgumentException("Vui long nhap ho va ten.");
        if (isBlank(user.getEmail()) || !isEmail(user.getEmail())) throw new IllegalArgumentException("Email khong hop le.");
        if (userDao.existsEmailExceptId(user.getEmail(), user.getId())) throw new IllegalArgumentException("Email da ton tai.");
        userDao.updateProfile(user);
    }
    @Override public void updateActive(int id, boolean active) { userDao.updateActive(id, active); }

    @Override
    public void registerPublic(User user) {
        normalize(user); validateNewUser(user);
        user.setRoleid(UserRole.CUSTOMER); user.setActive(true); user.setCreatedDate(new Date(System.currentTimeMillis()));
        userDao.insert(user);
    }

    @Override
    public void createByAdmin(User user) {
        normalize(user); validateNewUser(user); validateRole(user.getRoleid());
        user.setCreatedDate(new Date(System.currentTimeMillis())); userDao.insert(user);
    }

    @Override
    public void updateByAdmin(User user) {
        User stored = userDao.findById(user.getId());
        if (stored == null) throw new IllegalArgumentException("Không tìm thấy người dùng.");
        normalize(user);
        if (isBlank(user.getEmail()) || !isEmail(user.getEmail())) throw new IllegalArgumentException("Email không hợp lệ.");
        if (userDao.existsEmailExceptId(user.getEmail(), user.getId())) throw new IllegalArgumentException("Email đã tồn tại.");
        validateRole(user.getRoleid());
        if (stored.getRoleid() == UserRole.ADMIN && stored.isActive() && (user.getRoleid() != UserRole.ADMIN || !user.isActive()) && userDao.countActiveAdmins() <= 1) {
            throw new IllegalArgumentException("Không thể hạ quyền hoặc khóa ADMIN cuối cùng.");
        }
        userDao.update(user);
    }

    @Override
    public void changeActiveByAdmin(User actor, int targetId, boolean active) {
        User target = userDao.findById(targetId);
        if (target == null) throw new IllegalArgumentException("Không tìm thấy người dùng.");
        if (actor.getId() == targetId && !active) throw new IllegalArgumentException("Không thể tự khóa tài khoản đang đăng nhập.");
        if (target.getRoleid() == UserRole.ADMIN && target.isActive() && !active && userDao.countActiveAdmins() <= 1) throw new IllegalArgumentException("Không thể khóa ADMIN cuối cùng.");
        userDao.updateActive(targetId, active);
    }

    private void validateNewUser(User user) {
        if (isBlank(user.getUserName()) || user.getUserName().length() < 3) throw new IllegalArgumentException("Username phải có ít nhất 3 ký tự.");
        if (isBlank(user.getPassword()) || user.getPassword().length() < 3) throw new IllegalArgumentException("Mật khẩu phải có ít nhất 3 ký tự.");
        if (isBlank(user.getEmail()) || !isEmail(user.getEmail())) throw new IllegalArgumentException("Email không hợp lệ.");
        if (userDao.checkExistUsername(user.getUserName())) throw new IllegalArgumentException("Username đã tồn tại.");
        if (userDao.checkExistEmail(user.getEmail())) throw new IllegalArgumentException("Email đã tồn tại.");
    }

    private void validateRole(int roleId) { if (!UserRole.isValid(roleId)) throw new IllegalArgumentException("Role chỉ có thể là ADMIN, MANAGER hoặc CUSTOMER."); }
    private void normalize(User user) { user.setUserName(trim(user.getUserName())); user.setEmail(trim(user.getEmail())); user.setFullName(trim(user.getFullName())); user.setPhone(trim(user.getPhone())); }
    private String trim(String value) { return value == null ? "" : value.trim(); }
    private boolean isBlank(String value) { return value == null || value.trim().isEmpty(); }
    private boolean isEmail(String value) { return value.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"); }
}
