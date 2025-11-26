package com.example.app.controller;

import com.example.app.dao.ArticleDAO;
import com.example.app.dao.UserDAO;
import com.example.app.dao.CategoryDAO;
import com.example.app.model.Article;
import com.example.app.model.User;
import com.example.app.model.Category;
import com.example.app.enums.ArticleStatus;
import com.example.app.enums.UserRole;
import com.example.app.enums.UserStatus;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet(name = "AdminServlet", urlPatterns = {
        "/admin",
        "/admin/articles",
        "/admin/approve",
        "/admin/reject",
        "/admin/remove-requests",
        "/admin/handle-remove",
        "/admin/users",
        "/admin/block-user",
        "/admin/unblock-user",
        "/admin/cleanup-users",
        "/admin/categories",
        "/admin/add-category",
        "/admin/edit-category",
        "/admin/delete-category"
})
public class AdminServlet extends HttpServlet {

    private ArticleDAO articleDAO = new ArticleDAO();
    private UserDAO userDAO = new UserDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra quyền Admin
        if (!checkAdminRole(request, response)) return;

        String path = request.getServletPath();

        switch (path) {
            case "/admin":
                showDashboard(request, response);
                break;

            case "/admin/articles":
                showArticlesManagement(request, response);
                break;

            case "/admin/remove-requests":
                showRemoveRequests(request, response);
                break;

            case "/admin/users":
                showUsersManagement(request, response);
                break;

            case "/admin/cleanup-users":
                showCleanupUsers(request, response);
                break;

            case "/admin/categories":
                showCategoriesManagement(request, response);
                break;

            case "/admin/edit-category":
                showEditCategory(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra quyền Admin
        if (!checkAdminRole(request, response)) return;

        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();

        switch (path) {
            case "/admin/approve":
                handleApproveArticle(request, response);
                break;

            case "/admin/reject":
                handleRejectArticle(request, response);
                break;

            case "/admin/handle-remove":
                handleRemoveRequest(request, response);
                break;

            case "/admin/block-user":
                handleBlockUser(request, response);
                break;

            case "/admin/unblock-user":
                handleUnblockUser(request, response);
                break;

            case "/admin/cleanup-users":
                handleCleanupUsers(request, response);
                break;

            case "/admin/add-category":
                handleAddCategory(request, response);
                break;

            case "/admin/edit-category":
                handleEditCategory(request, response);
                break;

            case "/admin/delete-category":
                handleDeleteCategory(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ============ HELPER METHODS ============

    // Kiểm tra quyền Admin
    private boolean checkAdminRole(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRole() != UserRole.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/home");
            return false;
        }
        return true;
    }

    // Dashboard - Trang chủ Admin
    private void showDashboard(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Thống kê tổng quan
        int totalArticles = articleDAO.countAll();
        int totalUsers = userDAO.countAll();
        int pendingArticles = articleDAO.countByStatus(ArticleStatus.PENDING);
        int publishedArticles = articleDAO.countByStatus(ArticleStatus.PUBLISHED);
        int removeRequests = articleDAO.countByStatus(ArticleStatus.REMOVE_PENDING);

        request.setAttribute("totalArticles", totalArticles);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("pendingArticles", pendingArticles);
        request.setAttribute("publishedArticles", publishedArticles);
        request.setAttribute("removeRequests", removeRequests);

        request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
    }

    // Quản lý bài viết
    private void showArticlesManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String statusFilter = request.getParameter("status");
        List<Article> articles;

        if (statusFilter != null && !statusFilter.isEmpty()) {
            // Lọc theo trạng thái (nếu có tham số)
            // Cần implement thêm phương thức findByStatus trong DAO nếu cần
            articles = articleDAO.findAllForAdmin();
        } else {
            articles = articleDAO.findAllForAdmin();
        }

        request.setAttribute("articles", articles);
        request.getRequestDispatcher("/WEB-INF/views/admin/articles.jsp").forward(request, response);
    }

    // Hiển thị danh sách yêu cầu gỡ bài
    private void showRemoveRequests(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Article> removeRequests = articleDAO.findRemovePending();
        request.setAttribute("removeRequests", removeRequests);
        request.getRequestDispatcher("/WEB-INF/views/admin/remove-requests.jsp").forward(request, response);
    }

    // Quản lý người dùng
    private void showUsersManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> users = userDAO.findAll();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
    }

    // Hiển thị danh sách user cần dọn dẹp (không online 2 tháng)
    private void showCleanupUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> inactiveUsers = userDAO.findInactiveUsers(2); // 2 tháng
        request.setAttribute("inactiveUsers", inactiveUsers);
        request.getRequestDispatcher("/WEB-INF/views/admin/cleanup-users.jsp").forward(request, response);
    }

    // Quản lý danh mục
    private void showCategoriesManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(request, response);
    }

    // Hiển thị form sửa category
    private void showEditCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Category category = categoryDAO.findById(id);

            if (category == null) {
                response.sendRedirect("categories?error=not_found");
                return;
            }

            request.setAttribute("category", category);
            request.getRequestDispatcher("/WEB-INF/views/admin/edit-category.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // ============ POST HANDLERS ============

    // Duyệt bài viết
    private void handleApproveArticle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int articleId = Integer.parseInt(request.getParameter("id"));

            if (articleDAO.updateStatus(articleId, ArticleStatus.PUBLISHED, null)) {
                response.sendRedirect("articles?msg=approved");
            } else {
                response.sendRedirect("articles?error=approve_failed");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Từ chối bài viết
    private void handleRejectArticle(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int articleId = Integer.parseInt(request.getParameter("id"));
            String reason = request.getParameter("reason");

            if (reason == null || reason.trim().isEmpty()) {
                response.sendRedirect("articles?error=reason_required");
                return;
            }

            if (articleDAO.updateStatus(articleId, ArticleStatus.REJECTED, reason)) {
                response.sendRedirect("articles?msg=rejected");
            } else {
                response.sendRedirect("articles?error=reject_failed");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Xử lý yêu cầu gỡ bài
    private void handleRemoveRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int articleId = Integer.parseInt(request.getParameter("id"));
            String action = request.getParameter("action"); // "approve" hoặc "reject"
            String adminMessage = request.getParameter("adminMessage");

            boolean approve = "approve".equals(action);

            if (articleDAO.handleRemoveRequest(articleId, approve, adminMessage)) {
                response.sendRedirect("remove-requests?msg=handled");
            } else {
                response.sendRedirect("remove-requests?error=handle_failed");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Khóa user
    private void handleBlockUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("id"));

            if (userDAO.updateStatus(userId, UserStatus.BLOCKED)) {
                response.sendRedirect("users?msg=blocked");
            } else {
                response.sendRedirect("users?error=block_failed");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Mở khóa user
    private void handleUnblockUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("id"));

            if (userDAO.updateStatus(userId, UserStatus.ACTIVE)) {
                response.sendRedirect("users?msg=unblocked");
            } else {
                response.sendRedirect("users?error=unblock_failed");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Dọn dẹp user không active
    private void handleCleanupUsers(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String[] userIds = request.getParameterValues("userIds");

        if (userIds == null || userIds.length == 0) {
            response.sendRedirect("cleanup-users?error=no_selection");
            return;
        }

        List<Integer> ids = new ArrayList<>();
        for (String id : userIds) {
            try {
                ids.add(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                // Bỏ qua ID không hợp lệ
            }
        }

        int deletedCount = userDAO.deleteMultipleUsers(ids);
        response.sendRedirect("cleanup-users?msg=deleted&count=" + deletedCount);
    }

    // Thêm category mới
    private void handleAddCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String name = request.getParameter("name");
        String description = request.getParameter("description");

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);

        if (categoryDAO.save(category)) {
            response.sendRedirect("categories?msg=added");
        } else {
            response.sendRedirect("categories?error=add_failed");
        }
    }

    // Sửa category
    private void handleEditCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");

            Category category = new Category();
            category.setId(id);
            category.setName(name);
            category.setDescription(description);

            if (categoryDAO.update(category)) {
                response.sendRedirect("categories?msg=updated");
            } else {
                response.sendRedirect("categories?error=update_failed");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Xóa category
    private void handleDeleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));

            if (categoryDAO.delete(id)) {
                response.sendRedirect("categories?msg=deleted");
            } else {
                response.sendRedirect("categories?error=has_articles");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }
}