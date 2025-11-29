package com.example.app.dao;

import com.example.app.model.Category;
import com.example.app.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDAO {

    // Lấy tất cả danh mục (Dùng cho menu và dropdown list)
    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm danh mục mới (Dành cho Admin)
    public boolean save(Category category) {
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Có thể lỗi trùng tên (Unique Key)
            return false;
        }
    }

    // Xóa danh mục (Cần cẩn thận với ràng buộc khóa ngoại)
    public boolean delete(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Lỗi này xảy ra nếu danh mục đang chứa bài viết (Foreign Key Constraint)
            System.err.println("Không thể xóa danh mục ID " + id + " vì đang có bài viết liên kết.");
            return false;
        }
    }

    // Tìm theo ID (để sửa)
    public Category findById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Category(rs.getInt("id"), rs.getString("name"), rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật danh mục
    public boolean update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<Integer, Integer> getCategoryArticleCount() {
        Map<Integer, Integer> countMap = new HashMap<>();
        String sql = "SELECT category_id, COUNT(*) as article_count " +
                "FROM articles " +
                "WHERE status = 'PUBLISHED' " +
                "GROUP BY category_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                int count = rs.getInt("article_count");
                countMap.put(categoryId, count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return countMap;
    }


}