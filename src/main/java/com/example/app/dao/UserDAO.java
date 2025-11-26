package com.example.app.dao;

import com.example.app.model.User;
import com.example.app.enums.UserStatus;
import com.example.app.enums.UserRole;
import com.example.app.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // 1. Hàm kiểm tra đăng nhập
    // Trả về đối tượng User nếu đúng, null nếu sai
    public User checkLogin(String username, String password) {
        User user = null;
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = 'ACTIVE'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // Lưu ý: Ở đây đang so sánh plain text. Sau này nên dùng MD5/BCrypt.

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    // 2. Hàm đăng ký tài khoản mới
    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password, full_name, email, role, status, created_at) VALUES (?, ?, ?, ?, ?, 'ACTIVE', NOW())";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            // Chuyển Enum sang String để lưu vào DB
            stmt.setString(5, UserRole.USER.name());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Hàm kiểm tra username đã tồn tại chưa (để validate khi đăng ký)
    public boolean isUsernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Nếu có dữ liệu -> Trả về true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Hàm cập nhật lần đăng nhập cuối (Cái này dùng cho chức năng lọc user 2 tháng)
    public void updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- HÀM PHỤ TRỢ: Map dữ liệu từ ResultSet sang User Object ---
    // Giúp code gọn hơn, đỡ phải viết đi viết lại đoạn này
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));

        // Xử lý Enum: Lấy String từ DB -> convert sang Enum Java
        try {
            user.setRole(UserRole.valueOf(rs.getString("role")));
            user.setStatus(UserStatus.valueOf(rs.getString("status")));
        } catch (IllegalArgumentException e) {
            // Trường hợp DB lưu sai giá trị enum thì set mặc định
            user.setRole(UserRole.USER);
        }

        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }

    // --- MAIN TEST: Chạy thử ngay tại đây ---
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();

        // Test 1: Đăng ký
        System.out.println("--- Test Đăng Ký ---");
        User newUser = new User();
        newUser.setUsername("test_user_1");
        newUser.setPassword("123456");
        newUser.setFullName("Nguyen Van Test");
        newUser.setEmail("test@gmail.com");

        if (dao.isUsernameExists("test_user_1")) {
            System.out.println("User đã tồn tại, bỏ qua đăng ký.");
        } else {
            boolean regResult = dao.register(newUser);
            System.out.println("Đăng ký thành công: " + regResult);
        }

        // Test 2: Đăng nhập
        System.out.println("\n--- Test Đăng Nhập ---");
        User loginUser = dao.checkLogin("test_user_1", "123456");
        if (loginUser != null) {
            System.out.println("Login OK! Hello " + loginUser.getFullName());
            System.out.println("Role: " + loginUser.getRole());
        } else {
            System.out.println("Login thất bại!");
        }
    }
}