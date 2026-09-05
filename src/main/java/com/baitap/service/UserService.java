package com.baitap.service;

import com.baitap.model.User;

public interface UserService {
    User findByUsername(String username);

    boolean authenticate(String username, String password);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);

    void insert(User user);

    java.util.List<User> search(String keyword, Integer roleid, Boolean active);

    User findById(int id);

    void update(User user);

    void updateProfile(User user);

    void updateActive(int id, boolean active);

    void registerPublic(User user);

    void createByAdmin(User user);

    void updateByAdmin(User user);

    void changeActiveByAdmin(User actor, int targetId, boolean active);
}
