package com.example.app.controller;

import com.example.app.dao.ArticleDAO;
import com.example.app.dao.CategoryDAO;
import com.example.app.model.Article;
import com.example.app.model.Category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home", "/", "/article-detail", "/category", "/search"})
public class HomeServlet extends HttpServlet {

    private ArticleDAO articleDAO = new ArticleDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Xử lý theo đường dẫn
        switch (path) {
            case "/article-detail":
                showArticleDetail(request, response);
                break;
            case "/category":
                showCategory(request, response);
                break;
            case "/search":
                handleSearch(request, response);
                break;
            default:
                showHomePage(request, response);
                break;
        }
    }

    // Hiển thị trang chủ
    private void showHomePage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy tham số category từ URL (nếu có)
        String categoryParam = request.getParameter("category");

        List<Article> articles;
        Category currentCategory = null;

        if (categoryParam != null && !categoryParam.isEmpty()) {
            // Lọc theo category
            int categoryId = Integer.parseInt(categoryParam);
            articles = articleDAO.findByCategory(categoryId);
            currentCategory = categoryDAO.findById(categoryId);
        } else {
            // Hiển thị tất cả bài viết đã publish
            articles = articleDAO.findAllPublished();
        }

        // Lấy danh sách tất cả categories
        List<Category> categories = categoryDAO.findAll();

        // Lấy số lượng bài viết theo từng category
        Map<Integer, Integer> categoryCount = categoryDAO.getCategoryArticleCount();

        // Lấy tin nổi bật (top 5 bài có lượt xem cao nhất)
        List<Article> featuredArticles = articleDAO.findTopViewed(5);

        // Gửi dữ liệu sang JSP
        request.setAttribute("articles", articles);
        request.setAttribute("categories", categories);
        request.setAttribute("categoryCount", categoryCount);
        request.setAttribute("currentCategory", currentCategory);
        request.setAttribute("featuredArticles", featuredArticles);

        request.getRequestDispatcher("/WEB-INF/views/web/home.jsp").forward(request, response);
    }

    // Hiển thị chi tiết bài viết
    private void showArticleDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        try {
            int articleId = Integer.parseInt(idParam);

            // Lấy chi tiết bài viết
            Article article = articleDAO.findById(articleId);

            if (article == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bài viết không tồn tại");
                return;
            }

            // Chỉ hiển thị bài viết đã PUBLISHED (trừ khi là admin hoặc author)
            if (!"PUBLISHED".equals(article.getStatus().name())) {
                // Kiểm tra xem user hiện tại có phải là tác giả hoặc admin không
                boolean isOwner = false;
                boolean isAdmin = false;

                if (request.getSession().getAttribute("currentUser") != null) {
                    com.example.app.model.User currentUser =
                            (com.example.app.model.User) request.getSession().getAttribute("currentUser");
                    isOwner = (currentUser.getId() == article.getUserId());
                    isAdmin = "ADMIN".equals(currentUser.getRole().name());
                }

                if (!isOwner && !isAdmin) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bài viết chưa được công bố");
                    return;
                }
            }

            // Tăng lượt xem (chỉ tăng cho bài PUBLISHED)
            if ("PUBLISHED".equals(article.getStatus().name())) {
                articleDAO.incrementViews(articleId);
                article.setViews(article.getViews() + 1);
            }

            // Lấy danh sách categories cho sidebar
            List<Category> categories = categoryDAO.findAll();

            // Lấy số lượng bài viết theo từng category
            Map<Integer, Integer> categoryCount = categoryDAO.getCategoryArticleCount();

            // Lấy tin nổi bật
            List<Article> featuredArticles = articleDAO.findTopViewed(5);

            // Lấy bài viết liên quan (cùng category, trừ bài hiện tại)
            List<Article> relatedArticles = articleDAO.findByCategory(article.getCategoryId());
            relatedArticles.removeIf(a -> a.getId() == articleId);

            if (relatedArticles.size() > 3) {
                relatedArticles = relatedArticles.subList(0, 3);
            }

            // Gửi dữ liệu sang JSP
            request.setAttribute("article", article);
            request.setAttribute("categories", categories);
            request.setAttribute("categoryCount", categoryCount);
            request.setAttribute("featuredArticles", featuredArticles);
            request.setAttribute("relatedArticles", relatedArticles);

            request.getRequestDispatcher("/WEB-INF/views/web/article-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    // Hiển thị bài viết theo category
    private void showCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String categoryParam = request.getParameter("id");

        if (categoryParam == null || categoryParam.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        try {
            int categoryId = Integer.parseInt(categoryParam);

            // Lấy thông tin category
            Category currentCategory = categoryDAO.findById(categoryId);

            if (currentCategory == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Danh mục không tồn tại");
                return;
            }

            // Lấy bài viết thuộc category này
            List<Article> articles = articleDAO.findByCategory(categoryId);

            // Lấy tất cả categories
            List<Category> categories = categoryDAO.findAll();

            // Lấy số lượng bài viết theo từng category
            Map<Integer, Integer> categoryCount = categoryDAO.getCategoryArticleCount();

            // Lấy tin nổi bật
            List<Article> featuredArticles = articleDAO.findTopViewed(5);

            // Gửi dữ liệu sang JSP
            request.setAttribute("currentCategory", currentCategory);
            request.setAttribute("articles", articles);
            request.setAttribute("categories", categories);
            request.setAttribute("categoryCount", categoryCount);
            request.setAttribute("featuredArticles", featuredArticles);

            request.getRequestDispatcher("/WEB-INF/views/web/home.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    // Tìm kiếm bài viết
    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        if (keyword == null || keyword.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        // Tìm kiếm bài viết
        List<Article> articles = articleDAO.search(keyword.trim());

        // Lấy categories
        List<Category> categories = categoryDAO.findAll();

        // Lấy số lượng bài viết theo từng category
        Map<Integer, Integer> categoryCount = categoryDAO.getCategoryArticleCount();

        // Lấy tin nổi bật
        List<Article> featuredArticles = articleDAO.findTopViewed(5);

        // Gửi dữ liệu sang JSP
        request.setAttribute("keyword", keyword);
        request.setAttribute("articles", articles);
        request.setAttribute("categories", categories);
        request.setAttribute("categoryCount", categoryCount);
        request.setAttribute("featuredArticles", featuredArticles);

        request.getRequestDispatcher("/WEB-INF/views/web/search-results.jsp").forward(request, response);
    }
}