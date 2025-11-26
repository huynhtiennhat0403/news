package com.example.app.controller;

import com.example.app.dao.ArticleDAO;
import com.example.app.dao.CategoryDAO;
import com.example.app.model.Article;
import com.example.app.model.Category;
import com.example.app.model.User;
import com.example.app.enums.ArticleStatus;
import com.example.app.utils.Upload;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ArticleServlet", urlPatterns = {
        "/write-article",
        "/save-article",
        "/edit-article",
        "/update-article",
        "/my-articles",
        "/delete-article",
        "/request-remove"
})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ArticleServlet extends HttpServlet {

    private ArticleDAO articleDAO = new ArticleDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // Bắt buộc phải đăng nhập
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        switch (path) {
            case "/write-article":
                showWriteArticlePage(request, response);
                break;

            case "/edit-article":
                showEditArticlePage(request, response, currentUser);
                break;

            case "/my-articles":
                showMyArticles(request, response, currentUser);
                break;

            case "/delete-article":
                handleDeleteArticle(request, response, currentUser);
                break;

            case "/request-remove":
                showRequestRemovePage(request, response, currentUser);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        switch (path) {
            case "/save-article":
                handleSaveArticle(request, response, currentUser);
                break;

            case "/update-article":
                handleUpdateArticle(request, response, currentUser);
                break;

            case "/request-remove":
                handleRequestRemove(request, response, currentUser);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // ============ HELPER METHODS ============

    // Hiển thị trang viết bài mới
    private void showWriteArticlePage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/user/write-article.jsp").forward(request, response);
    }

    // Hiển thị trang chỉnh sửa bài viết
    private void showEditArticlePage(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        try {
            int articleId = Integer.parseInt(request.getParameter("id"));

            // Kiểm tra quyền sở hữu
            if (!articleDAO.isOwner(articleId, currentUser.getId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền sửa bài này");
                return;
            }

            // Kiểm tra có được phép sửa không (DRAFT, PENDING, REJECTED)
            if (!articleDAO.canEdit(articleId, currentUser.getId())) {
                request.setAttribute("error", "Bài viết đã PUBLISHED, không thể sửa!");
                request.getRequestDispatcher("/WEB-INF/views/user/my-articles.jsp").forward(request, response);
                return;
            }

            Article article = articleDAO.findById(articleId);
            List<Category> categories = categoryDAO.findAll();

            request.setAttribute("article", article);
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/WEB-INF/views/user/edit-article.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Hiển thị danh sách bài viết của user
    private void showMyArticles(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        List<Article> myArticles = articleDAO.findByAuthor(currentUser.getId());
        request.setAttribute("myArticles", myArticles);
        request.getRequestDispatcher("/WEB-INF/views/user/my-articles.jsp").forward(request, response);
    }

    // Xóa bài viết
    private void handleDeleteArticle(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));

            // Kiểm tra quyền sở hữu
            if (!articleDAO.isOwner(id, currentUser.getId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xóa bài này");
                return;
            }

            // Kiểm tra có được phép xóa không
            if (!articleDAO.canEdit(id, currentUser.getId())) {
                response.sendRedirect("my-articles?error=cannot_delete");
                return;
            }

            // Xóa bài
            if (articleDAO.delete(id)) {
                response.sendRedirect("my-articles?msg=deleted");
            } else {
                response.sendRedirect("my-articles?error=delete_failed");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Hiển thị trang yêu cầu gỡ bài
    private void showRequestRemovePage(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {
        try {
            int articleId = Integer.parseInt(request.getParameter("id"));

            // Kiểm tra quyền sở hữu
            if (!articleDAO.isOwner(articleId, currentUser.getId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền với bài này");
                return;
            }

            // Kiểm tra bài có đang PUBLISHED không
            if (!articleDAO.canRequestRemove(articleId, currentUser.getId())) {
                response.sendRedirect("my-articles?error=not_published");
                return;
            }

            Article article = articleDAO.findById(articleId);
            request.setAttribute("article", article);
            request.getRequestDispatcher("/WEB-INF/views/user/request-remove.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Lưu bài viết mới
    private void handleSaveArticle(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {

        String title = request.getParameter("title");
        String shortDesc = request.getParameter("shortDescription");
        String content = request.getParameter("content");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String action = request.getParameter("action"); // "save_draft" hoặc "submit_pending"

        // Xử lý Upload ảnh
        Part filePart = request.getPart("thumbnail");
        String thumbnailPath = null;
        if (filePart != null && filePart.getSize() > 0) {
            String realPath = request.getServletContext().getRealPath("");
            thumbnailPath = Upload.saveFile(filePart, realPath);
        }

        Article article = new Article();
        article.setTitle(title);
        article.setShortDescription(shortDesc);
        article.setContent(content);
        article.setThumbnail(thumbnailPath);
        article.setCategoryId(categoryId);
        article.setUserId(currentUser.getId());

        // Logic trạng thái
        if ("submit_pending".equals(action)) {
            article.setStatus(ArticleStatus.PENDING);
        } else {
            article.setStatus(ArticleStatus.DRAFT);
        }

        // Gọi DAO lưu
        if (articleDAO.save(article)) {
            response.sendRedirect("my-articles?msg=success");
        } else {
            request.setAttribute("error", "Lỗi khi lưu bài viết");
            showWriteArticlePage(request, response);
        }
    }

    // Cập nhật bài viết đã có
    private void handleUpdateArticle(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {

        try {
            int articleId = Integer.parseInt(request.getParameter("id"));

            // Kiểm tra quyền
            if (!articleDAO.isOwner(articleId, currentUser.getId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            if (!articleDAO.canEdit(articleId, currentUser.getId())) {
                response.sendRedirect("my-articles?error=cannot_edit");
                return;
            }

            // Lấy thông tin cũ
            Article article = articleDAO.findById(articleId);

            // Cập nhật thông tin mới
            article.setTitle(request.getParameter("title"));
            article.setShortDescription(request.getParameter("shortDescription"));
            article.setContent(request.getParameter("content"));
            article.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));

            // Xử lý Upload ảnh mới (nếu có)
            Part filePart = request.getPart("thumbnail");
            if (filePart != null && filePart.getSize() > 0) {
                String realPath = request.getServletContext().getRealPath("");
                String thumbnailPath = Upload.saveFile(filePart, realPath);
                article.setThumbnail(thumbnailPath);
            }

            // Cập nhật trạng thái nếu user chọn gửi duyệt
            String action = request.getParameter("action");
            if ("submit_pending".equals(action)) {
                article.setStatus(ArticleStatus.PENDING);
            }

            // Lưu cập nhật
            if (articleDAO.update(article)) {
                response.sendRedirect("my-articles?msg=updated");
            } else {
                response.sendRedirect("my-articles?error=update_failed");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Xử lý yêu cầu gỡ bài
    private void handleRequestRemove(HttpServletRequest request, HttpServletResponse response, User currentUser)
            throws ServletException, IOException {

        try {
            int articleId = Integer.parseInt(request.getParameter("id"));
            String userMessage = request.getParameter("userMessage");

            // Kiểm tra quyền
            if (!articleDAO.isOwner(articleId, currentUser.getId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            if (!articleDAO.canRequestRemove(articleId, currentUser.getId())) {
                response.sendRedirect("my-articles?error=not_published");
                return;
            }

            // Gửi yêu cầu gỡ bài
            if (articleDAO.requestRemove(articleId, userMessage)) {
                response.sendRedirect("my-articles?msg=remove_requested");
            } else {
                response.sendRedirect("my-articles?error=request_failed");
            }

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }
}