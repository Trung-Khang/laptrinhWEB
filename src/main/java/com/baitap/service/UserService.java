package com.baitap.service;

import com.baitap.model.User;

public interface UserService {
    User findByUsername(String username);

    boolean authenticate(String username, String password);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);

    void insert(User user);
}