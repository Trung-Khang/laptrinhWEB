package com.baitap.service.impl;

import com.baitap.dao.UserDao;
import com.baitap.dao.impl.UserDaoImpl;
import com.baitap.model.User;
import com.baitap.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public boolean authenticate(String username, String password) {
        User user = userDao.findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return userDao.checkExistEmail(email);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return userDao.checkExistUsername(username);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return userDao.checkExistPhone(phone);
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }
}