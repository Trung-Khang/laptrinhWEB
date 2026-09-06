package com.baitap.otp;

import com.baitap.connection.DBConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/** JDBC repository because account and OTP data share the legacy User table. */
public class AccountOtpDao {
    private static final int MAX_ATTEMPTS = 5;
    private static final Logger LOGGER = Logger.getLogger(AccountOtpDao.class.getName());

    public OtpIssueStatus issueStatus(int userId, OtpPurpose purpose) {
        String sql = "SELECT CASE "
                + "WHEN EXISTS (SELECT 1 FROM dbo.account_otps WHERE user_id=? AND purpose=? AND created_at > DATEADD(SECOND, -60, SYSUTCDATETIME())) THEN 'TOO_SOON' "
                + "WHEN (SELECT COUNT(*) FROM dbo.account_otps WHERE user_id=? AND purpose=? AND created_at > DATEADD(HOUR, -1, SYSUTCDATETIME())) >= 5 THEN 'RATE_LIMITED' "
                + "ELSE 'ALLOWED' END";
        try (Connection connection = new DBConnection().getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId); statement.setString(2, purpose.name());
            statement.setInt(3, userId); statement.setString(4, purpose.name());
            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return OtpIssueStatus.valueOf(result.getString(1));
            }
        } catch (Exception exception) { throw dataAccess(exception); }
    }

    public void create(int userId, OtpPurpose purpose, String otpHash) {
        String invalidate = "UPDATE dbo.account_otps SET used_at=SYSUTCDATETIME() WHERE user_id=? AND purpose=? AND used_at IS NULL";
        String insert = "INSERT INTO dbo.account_otps (user_id, purpose, otp_hash, expires_at, attempt_count, created_at) VALUES (?, ?, ?, DATEADD(MINUTE, 5, SYSUTCDATETIME()), 0, SYSUTCDATETIME())";
        try (Connection connection = new DBConnection().getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(invalidate)) {
                statement.setInt(1, userId); statement.setString(2, purpose.name()); statement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(insert)) {
                statement.setInt(1, userId); statement.setString(2, purpose.name()); statement.setString(3, otpHash); statement.executeUpdate();
            }
            connection.commit();
        } catch (Exception exception) { throw dataAccess(exception); }
    }

    public OtpVerifyResult verify(int userId, OtpPurpose purpose, String providedHash) {
        String select = "SELECT TOP 1 id, otp_hash, attempt_count, CASE WHEN expires_at <= SYSUTCDATETIME() THEN 1 ELSE 0 END AS is_expired "
                + "FROM dbo.account_otps WITH (UPDLOCK, ROWLOCK) WHERE user_id=? AND purpose=? AND used_at IS NULL ORDER BY id DESC";
        try (Connection connection = new DBConnection().getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(select)) {
                statement.setInt(1, userId); statement.setString(2, purpose.name());
                try (ResultSet result = statement.executeQuery()) {
                    if (!result.next()) { LOGGER.info("OTP is missing or already used for account id " + userId + " and purpose " + purpose.name()); connection.commit(); return OtpVerifyResult.INVALID; }
                    int id = result.getInt("id");
                    int attempts = result.getInt("attempt_count");
                    if (result.getInt("is_expired") == 1) {
                        markUsed(connection, id); LOGGER.info("OTP expired for account id " + userId + " and purpose " + purpose.name()); connection.commit(); return OtpVerifyResult.EXPIRED;
                    }
                    if (attempts >= MAX_ATTEMPTS) { markUsed(connection, id); LOGGER.warning("OTP attempts exceeded for account id " + userId + " and purpose " + purpose.name()); connection.commit(); return OtpVerifyResult.TOO_MANY_ATTEMPTS; }
                    byte[] expected = result.getString("otp_hash").getBytes(StandardCharsets.US_ASCII);
                    if (!MessageDigest.isEqual(expected, providedHash.getBytes(StandardCharsets.US_ASCII))) {
                        try (PreparedStatement failed = connection.prepareStatement("UPDATE dbo.account_otps SET attempt_count=attempt_count+1 WHERE id=?")) {
                            failed.setInt(1, id); failed.executeUpdate();
                        }
                        LOGGER.info("Invalid OTP attempt for account id " + userId + " and purpose " + purpose.name());
                        connection.commit(); return attempts + 1 >= MAX_ATTEMPTS ? OtpVerifyResult.TOO_MANY_ATTEMPTS : OtpVerifyResult.INVALID;
                    }
                    markUsed(connection, id);
                    if (purpose == OtpPurpose.REGISTER_VERIFY) {
                        try (PreparedStatement activate = connection.prepareStatement("UPDATE dbo.[User] SET email_verified=1, active=1 WHERE id=?")) {
                            activate.setInt(1, userId); activate.executeUpdate();
                        }
                    }
                    LOGGER.info("OTP verified for account id " + userId + " and purpose " + purpose.name());
                    connection.commit(); return OtpVerifyResult.VERIFIED;
                }
            } catch (Exception exception) {
                connection.rollback(); throw exception;
            }
        } catch (Exception exception) { throw dataAccess(exception); }
    }

    private void markUsed(Connection connection, int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE dbo.account_otps SET used_at=SYSUTCDATETIME() WHERE id=?")) {
            statement.setInt(1, id); statement.executeUpdate();
        }
    }

    private RuntimeException dataAccess(Exception exception) {
        return new IllegalStateException("Không thể xử lý mã xác thực. Vui lòng thử lại sau.", exception);
    }
}
