package com.baitap.dao;

import com.baitap.model.User;

public interface UserDao {
    User findByUsername(String username);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);

    void insert(User user);

    java.util.List<User> search(String keyword, Integer roleid, Boolean active);

    User findById(int id);

    void update(User user);

    void updateProfile(User user);

    void updateActive(int id, boolean active);

    boolean existsEmailExceptId(String email, int id);

    int countActiveAdmins();
}
