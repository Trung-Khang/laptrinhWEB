package com.baitap.profile;

import com.baitap.model.User;

public interface ProfileRepository {
    User updateProfile(User user);
}
