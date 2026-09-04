package com.baitap.dao.impl;

import com.baitap.connection.DBConnection;
import com.baitap.dao.UserDao;
import com.baitap.model.User;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM [User] WHERE username = ?";
        User user = findByUsername(sql, username, false);
        return user != null ? user : findByUsername(sql, username, true);
    }

    @Override
    public boolean checkExistUsername(String username) {
        return exists("SELECT 1 FROM [User] WHERE username = ?", username);
    }

    @Override
    public boolean checkExistEmail(String email) {
        return exists("SELECT 1 FROM [User] WHERE email = ?", email);
    }

    @Override
    public boolean checkExistPhone(String phone) {
        return exists("SELECT 1 FROM [User] WHERE phone = ?", phone);
    }

    @Override
    public void insert(User user) {
        String sql = "INSERT INTO [User] (username, email, fullname, password, avatar, roleid, phone, createddate, active) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String avatar = user.getAvatar() != null ? user.getAvatar() : "";
        int roleid = user.getRoleid() == 0 ? 3 : user.getRoleid();
        Date createdDate = user.getCreatedDate() != null ? user.getCreatedDate() : new Date(System.currentTimeMillis());

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
            ps.setBoolean(9, user.isActive());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> search(String keyword, Integer roleid, Boolean active) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM [User] WHERE (? IS NULL OR username LIKE ? OR fullname LIKE ? OR email LIKE ?) "
                   + "AND (? IS NULL OR roleid = ?) AND (? IS NULL OR active = ?) ORDER BY id DESC";
        String kw = isBlank(keyword) ? null : "%" + keyword.trim() + "%";

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            if (roleid == null) {
                ps.setNull(5, Types.INTEGER);
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(5, roleid);
                ps.setInt(6, roleid);
            }
            if (active == null) {
                ps.setNull(7, Types.BIT);
                ps.setNull(8, Types.BIT);
            } else {
                ps.setBoolean(7, active);
                ps.setBoolean(8, active);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) users.add(mapUser(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM [User] WHERE id = ?";
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE [User] SET email=?, fullname=?, roleid=?, phone=?, active=? WHERE id=?";
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullName());
            ps.setInt(3, user.getRoleid());
            ps.setString(4, user.getPhone());
            ps.setBoolean(5, user.isActive());
            ps.setInt(6, user.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateActive(int id, boolean active) {
        String sql = "UPDATE [User] SET active=? WHERE id=?";
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private User findByUsername(String sql, String username, boolean legacyDatabase) {
        try (Connection conn = legacyDatabase ? getLegacyConnection() : new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapUser(rs);
            }
        } catch (Exception e) {
            if (!legacyDatabase) e.printStackTrace();
        }
        return null;
    }

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
        try {
            user.setActive(rs.getBoolean("active"));
        } catch (Exception ignored) {
            user.setActive(true);
        }
        return user;
    }

    private Connection getLegacyConnection() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://127.0.0.1:52282;databaseName=DB_LapTrinhWeb;encrypt=false;trustServerCertificate=true";
        return DriverManager.getConnection(url, "sa", "trungkhang");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
