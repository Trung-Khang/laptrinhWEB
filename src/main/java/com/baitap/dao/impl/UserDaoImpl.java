package com.baitap.dao.impl;

import com.baitap.connection.DBConnection;
import com.baitap.dao.UserDao;
import com.baitap.model.User;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDaoImpl implements UserDao {

    @Override
    public User findByUsername(String username) {
        // Câu lệnh SQL lấy thông tin user dựa vào username
        String sql = "SELECT * FROM [User] WHERE username = ?";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapUser(rs); // Trả về thông tin user tìm được từ database
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Không tìm thấy hoặc lỗi
    }

    @Override
    public boolean checkExistUsername(String username) {
        String sql = "SELECT 1 FROM [User] WHERE username = ?";
        return exists(sql, username);
    }

    @Override
    public boolean checkExistEmail(String email) {
        String sql = "SELECT 1 FROM [User] WHERE email = ?";
        return exists(sql, email);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        String sql = "SELECT 1 FROM [User] WHERE phone = ?";
        return exists(sql, phone);
    }

    @Override
    public void insert(User user) {
        String sql = "INSERT INTO [User] (username, email, fullname, password, avatar, roleid, phone, createddate) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        // Giá trị mặc định hợp lý khi đối tượng User chưa được gán đầy đủ (vd: từ form đăng ký)
        String avatar = (user.getAvatar() != null) ? user.getAvatar() : "";
        int roleid = (user.getRoleid() == 0) ? 3 : user.getRoleid(); // 3 = người dùng thường
        Date createdDate = (user.getCreatedDate() != null) ? user.getCreatedDate() : new Date(System.currentTimeMillis());

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getPassword());
            ps.setString(5, avatar);
            ps.setInt(6, roleid);
            ps.setString(7, user.getPhone());
            ps.setDate(8, createdDate);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Trả về true nếu tồn tại ít nhất 1 dòng khớp giá trị truy vấn. */
    private boolean exists(String sql, String value) {
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, value);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Ánh xạ 1 dòng kết quả ResultSet sang đối tượng User. */
    private User mapUser(ResultSet rs) throws Exception {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setUserName(rs.getString("username"));
        user.setFullName(rs.getString("fullname"));
        user.setPassword(rs.getString("password"));
        user.setAvatar(rs.getString("avatar"));
        user.setRoleid(rs.getInt("roleid"));
        user.setPhone(rs.getString("phone"));
        user.setCreatedDate(rs.getDate("createddate"));
        return user;
    }
}