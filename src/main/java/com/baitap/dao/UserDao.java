package com.baitap.dao;

import com.baitap.model.User;

public interface UserDao {
    User findByUsername(String username);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);

    void insert(User user);
}