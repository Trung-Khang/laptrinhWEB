package com.baitap.profile;

import com.baitap.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig;

public class JpaProfileRepository implements ProfileRepository {
    @Override
    public User updateProfile(User user) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User managed = em.find(User.class, user.getId());
            if (managed == null) {
                throw new IllegalArgumentException("Khong tim thay nguoi dung.");
            }
            managed.setFullName(user.getFullName());
            managed.setPhone(user.getPhone());
            managed.setAvatar(user.getAvatar());
            managed.setEmail(user.getEmail());
            tx.commit();
            return detachCopy(managed);
        } catch (RuntimeException exception) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw exception;
        } finally {
            em.close();
        }
    }

    private User detachCopy(User source) {
        User copy = new User();
        copy.setId(source.getId());
        copy.setEmail(source.getEmail());
        copy.setUserName(source.getUserName());
        copy.setFullName(source.getFullName());
        copy.setPassword(source.getPassword());
        copy.setAvatar(source.getAvatar());
        copy.setRoleid(source.getRoleid());
        copy.setPhone(source.getPhone());
        copy.setCreatedDate(source.getCreatedDate());
        copy.setActive(source.isActive());
        copy.setEmailVerified(source.isEmailVerified());
        return copy;
    }
}
