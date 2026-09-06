package com.baitap.dao;

import com.baitap.model.User;

public interface UserDao {
    User findByUsername(String username);

    User findByEmail(String email);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);

    void insert(User user);

    java.util.List<User> search(String keyword, Integer roleid, Boolean active);

    User findById(int id);

    void update(User user);

    void updateProfile(User user);

    void updateActive(int id, boolean active);

    void updateEmailVerified(int id, boolean emailVerified);

    void updatePassword(int id, String passwordHash);

    boolean hasOrders(int id);

    void delete(int id);

    boolean existsEmailExceptId(String email, int id);

    int countActiveAdmins();
}
