package com.example.app.model;

import com.example.app.enums.ArticleStatus;
import java.sql.Timestamp;

public class Article {
    private int id;
    private String title;
    private String shortDescription;
    private String content;
    private String thumbnail;
    private ArticleStatus status;

    private String adminMessage; // Lý do từ chối
    private String userMessage;  // Lý do xin gỡ

    private int views;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Foreign Keys
    private int userId;
    private int categoryId;

    // Trường bổ sung để hiển thị trên View (Không lưu trong bảng articles)
    private String authorName;   // Tên tác giả
    private String categoryName; // Tên chuyên mục

    public Article() {}

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getThumbnail() { return thumbnail; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public ArticleStatus getStatus() { return status; }
    public void setStatus(ArticleStatus status) { this.status = status; }

    public String getAdminMessage() { return adminMessage; }
    public void setAdminMessage(String adminMessage) { this.adminMessage = adminMessage; }

    public String getUserMessage() { return userMessage; }
    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }

    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    // Getter Setter cho trường bổ sung
    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
