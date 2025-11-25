package com.example.app.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // --- CẤU HÌNH DATABASE ---
    // Tên database phải khớp với cái bạn đã tạo (newspaper_db)
    // useSSL=false: Tắt bảo mật SSL để tránh lỗi cảnh báo trên local
    // allowPublicKeyRetrieval=true: Cho phép lấy public key (fix lỗi kết nối MySQL 8.0+)
    private static final String URL = "jdbc:mysql://localhost:3306/newspaper_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    // ⚠️ LƯU Ý: Thay đổi username/password tương ứng với máy của bạn
    private static final String USER = "root";
    private static final String PASSWORD = "Bonbone@0403"; // Máy mình không pass, nếu máy bạn có pass (vd: 123456) thì điền vào đây

    // Hàm lấy kết nối
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 1. Nạp Driver MySQL (Quan trọng)
            // Nếu chạy bị lỗi ClassNotFoundException nghĩa là chưa add thư viện mysql-connector-j
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Tạo kết nối
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Lỗi: Không tìm thấy thư viện MySQL JDBC Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Lỗi: Không thể kết nối đến MySQL! Kiểm tra lại URL/User/Pass.");
            e.printStackTrace();
        }
        return conn;
    }

    // --- HÀM MAIN ĐỂ TEST KẾT NỐI (Chạy file này trực tiếp) ---
    public static void main(String[] args) {
        System.out.println("Đang thử kết nối đến database 'newspaper_db'...");

        Connection conn = getConnection();

        if (conn != null) {
            System.out.println("✅ KẾT NỐI THÀNH CÔNG! Database đã sẵn sàng phục vụ.");
            try {
                conn.close(); // Test xong thì đóng lại cho sạch
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("⛔ KẾT NỐI THẤT BẠI. Vui lòng xem log lỗi ở trên.");
        }
    }
}