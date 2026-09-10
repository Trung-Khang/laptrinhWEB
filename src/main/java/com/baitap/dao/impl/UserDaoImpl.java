package com.baitap.dao.impl;

import com.baitap.connection.DBConnection;
import com.baitap.dao.UserDao;
import com.baitap.model.User;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** JDBC access for the single application database: ShoppingServiceMVC. */
public class UserDaoImpl implements UserDao {
    private static final String USER_COLUMNS =
            "id, email, username, fullname, password, avatar, roleid, phone, createddate, active, email_verified";

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT " + USER_COLUMNS + " FROM dbo.[User] WHERE username = ?";
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapUser(rs) : null; }
        } catch (Exception e) { throw dataAccess("Không thể đọc tài khoản từ ShoppingServiceMVC.", e); }
    }

    @Override
    public User findByEmail(String email) {
        String sql = "SELECT " + USER_COLUMNS + " FROM dbo.[User] WHERE email = ?";
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapUser(rs) : null; }
        } catch (Exception e) { throw dataAccess("Không thể đọc tài khoản từ ShoppingServiceMVC.", e); }
    }

    @Override public boolean checkExistUsername(String username) { return exists("SELECT 1 FROM dbo.[User] WHERE username = ?", username); }
    @Override public boolean checkExistEmail(String email) { return exists("SELECT 1 FROM dbo.[User] WHERE email = ?", email); }
    @Override public boolean checkExistPhone(String phone) { return exists("SELECT 1 FROM dbo.[User] WHERE phone = ?", phone); }

    @Override
    public void insert(User user) {
        String sql = "INSERT INTO dbo.[User] (username, email, fullname, password, avatar, roleid, phone, createddate, active, email_verified) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);
            ps.setString(1, user.getUserName()); ps.setString(2, user.getEmail()); ps.setString(3, user.getFullName());
            ps.setString(4, user.getPassword()); ps.setString(5, user.getAvatar() == null ? "" : user.getAvatar());
            ps.setInt(6, user.getRoleid()); ps.setString(7, user.getPhone());
            ps.setDate(8, user.getCreatedDate() == null ? new Date(System.currentTimeMillis()) : user.getCreatedDate());
            ps.setBoolean(9, user.isActive()); ps.setBoolean(10, user.isEmailVerified()); ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) user.setId(keys.getInt(1)); }
            conn.commit();
        } catch (Exception e) { throw dataAccess("Không thể thêm người dùng vào ShoppingServiceMVC.", e); }
    }

    @Override
    public List<User> search(String keyword, Integer roleid, Boolean active) {
        List<User> users = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT ").append(USER_COLUMNS).append(" FROM dbo.[User] WHERE 1=1");
        List<Object> values = new ArrayList<>();
        if (!isBlank(keyword)) { sql.append(" AND (username LIKE ? OR fullname LIKE ? OR email LIKE ?)"); String value = "%" + keyword.trim() + "%"; values.add(value); values.add(value); values.add(value); }
        if (roleid != null) { sql.append(" AND roleid = ?"); values.add(roleid); }
        if (active != null) { sql.append(" AND active = ?"); values.add(active); }
        sql.append(" ORDER BY id DESC");
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < values.size(); i++) ps.setObject(i + 1, values.get(i));
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) users.add(mapUser(rs)); }
            return users;
        } catch (Exception e) { throw dataAccess("Không thể tải danh sách người dùng.", e); }
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT " + USER_COLUMNS + " FROM dbo.[User] WHERE id = ?";
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id); try (ResultSet rs = ps.executeQuery()) { return rs.next() ? mapUser(rs) : null; }
        } catch (Exception e) { throw dataAccess("Không thể đọc người dùng.", e); }
    }

    @Override
    public void update(User user) {
        boolean changePassword = !isBlank(user.getPassword());
        String sql = "UPDATE dbo.[User] SET email=?, fullname=?, roleid=?, phone=?, active=?" + (changePassword ? ", password=?" : "") + " WHERE id=?";
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); int index = 1;
            ps.setString(index++, user.getEmail()); ps.setString(index++, user.getFullName()); ps.setInt(index++, user.getRoleid());
            ps.setString(index++, user.getPhone()); ps.setBoolean(index++, user.isActive());
            if (changePassword) ps.setString(index++, user.getPassword());
            ps.setInt(index, user.getId());
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Không tìm thấy người dùng cần cập nhật.");
            conn.commit();
        } catch (Exception e) { throw dataAccess("Không thể cập nhật người dùng.", e); }
    }

    @Override
    public void updateProfile(User user) {
        String sql = "UPDATE dbo.[User] SET email=?, fullname=?, phone=? WHERE id=?";
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getPhone());
            ps.setInt(4, user.getId());
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Khong tim thay nguoi dung can cap nhat.");
        } catch (Exception e) { throw dataAccess("Khong the cap nhat ho so nguoi dung.", e); }
    }

    @Override
    public void updateActive(int id, boolean active) {
        String sql = "UPDATE dbo.[User] SET active=? WHERE id=?";
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, active); ps.setInt(2, id);
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Không tìm thấy người dùng cần cập nhật.");
        } catch (Exception e) { throw dataAccess("Không thể cập nhật trạng thái người dùng.", e); }
    }

    @Override
    public void updateEmailVerified(int id, boolean emailVerified) {
        String sql = "UPDATE dbo.[User] SET email_verified=? WHERE id=?";
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, emailVerified); ps.setInt(2, id);
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Không tìm thấy người dùng cần cập nhật.");
        } catch (Exception e) { throw dataAccess("Không thể cập nhật trạng thái xác minh email.", e); }
    }

    @Override
    public void updatePassword(int id, String passwordHash) {
        String sql = "UPDATE dbo.[User] SET password=? WHERE id=?";
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, passwordHash); ps.setInt(2, id);
            if (ps.executeUpdate() != 1) throw new IllegalArgumentException("Không tìm thấy người dùng cần cập nhật.");
        } catch (Exception e) { throw dataAccess("Không thể cập nhật mật khẩu.", e); }
    }

    @Override
    public boolean hasNonCancelledOrders(int id) {
        String sql = "SELECT CASE WHEN EXISTS (SELECT 1 FROM dbo.orders WHERE user_id=? AND (status IS NULL OR status <> 'CANCELLED')) THEN 1 ELSE 0 END";
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() && rs.getInt(1) == 1; }
        } catch (Exception e) { throw dataAccess("Không thể kiểm tra lịch sử đơn hàng của người dùng.", e); }
    }

    @Override
    public void delete(int id) {
        try (Connection conn = new DBConnection().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement orderItems = conn.prepareStatement(
                         "DELETE oi FROM dbo.order_items oi INNER JOIN dbo.orders o ON o.id = oi.order_id WHERE o.user_id=?");
                 PreparedStatement orders = conn.prepareStatement("DELETE FROM dbo.orders WHERE user_id=?");
                 PreparedStatement otp = conn.prepareStatement("DELETE FROM dbo.account_otps WHERE user_id=?");
                 PreparedStatement user = conn.prepareStatement("DELETE FROM dbo.[User] WHERE id=?")) {
                orderItems.setInt(1, id); orderItems.executeUpdate();
                orders.setInt(1, id); orders.executeUpdate();
                otp.setInt(1, id); otp.executeUpdate();
                user.setInt(1, id);
                if (user.executeUpdate() != 1) throw new IllegalArgumentException("Không tìm thấy người dùng cần xóa.");
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) { throw dataAccess("Không thể xóa người dùng.", e); }
    }

    @Override
    public boolean existsEmailExceptId(String email, int id) {
        String sql = "SELECT 1 FROM dbo.[User] WHERE email = ? AND id <> ?";
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim()); ps.setInt(2, id); try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (Exception e) { throw dataAccess("Không thể kiểm tra email.", e); }
    }

    @Override
    public int countActiveAdmins() {
        String sql = "SELECT COUNT(*) FROM dbo.[User] WHERE roleid = 1 AND active = 1";
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) { throw dataAccess("Không thể kiểm tra tài khoản quản trị.", e); }
    }

    private boolean exists(String sql, String value) {
        if (isBlank(value)) return false;
        try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, value.trim()); try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        } catch (Exception e) { throw dataAccess("Không thể kiểm tra dữ liệu người dùng.", e); }
    }

    private User mapUser(ResultSet rs) throws Exception {
        User user = new User(); user.setId(rs.getInt("id")); user.setEmail(rs.getString("email"));
        user.setUserName(rs.getString("username")); user.setFullName(rs.getString("fullname")); user.setPassword(rs.getString("password"));
        user.setAvatar(rs.getString("avatar")); user.setRoleid(rs.getInt("roleid")); user.setPhone(rs.getString("phone"));
        user.setCreatedDate(rs.getDate("createddate")); user.setActive(rs.getBoolean("active")); user.setEmailVerified(rs.getBoolean("email_verified")); return user;
    }

    private RuntimeException dataAccess(String message, Exception cause) { return new IllegalStateException(message, cause); }
    private boolean isBlank(String value) { return value == null || value.trim().isEmpty(); }
}
