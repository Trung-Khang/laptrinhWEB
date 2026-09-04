package com.baitap.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://127.0.0.1:52282;databaseName=ShoppingServiceMVC;encrypt=false;trustServerCertificate=true";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        String userID = "sa"; 
        String password = "trungkhang"; // Đổi thành mật khẩu sa của bạn
        
        return DriverManager.getConnection(url, userID, password);
    }

public static void main(String[] args) {
        try {
            DBConnection db = new DBConnection();
            Connection conn = db.getConnection();
            if (conn != null) {
                System.out.println("🎉 KẾT NỐI DATABASE THÀNH CÔNG!");
            }
        } catch (Exception e) {
            System.out.println("❌ KẾT NỐI THẤT BẠI. Xem lỗi chi tiết bên dưới:");
            e.printStackTrace();
        }
    }
}
