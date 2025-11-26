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

@WebServlet(name = "HomeServlet", urlPatterns = {"/home", "/", "/article-detail", "/category", "/search"})
public class HomeServlet extends HttpServlet {

    private ArticleDAO articleDAO = new ArticleDAO();
    private CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        switch (path) {
            case "/":
            case "/home":
                showHomePage(request, response);
                break;

            case "/article-detail":
                showArticleDetail(request, response);
                break;

            case "/category":
                showArticlesByCategory(request, response);
                break;

            case "/search":
                handleSearch(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    // Hiển thị trang chủ với danh sách bài viết đã đăng
    private void showHomePage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy danh sách tất cả bài viết đã PUBLISHED
        List<Article> articles = articleDAO.findAllPublished();

        // Lấy danh sách category cho menu
        List<Category> categories = categoryDAO.findAll();

        // Truyền dữ liệu sang JSP
        request.setAttribute("articles", articles);
        request.setAttribute("categories", categories);

        // Forward sang trang chủ
        request.getRequestDispatcher("/WEB-INF/views/web/home.jsp").forward(request, response);
    }

    // Hiển thị chi tiết một bài viết
    private void showArticleDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int articleId = Integer.parseInt(request.getParameter("id"));

            // Lấy thông tin bài viết
            Article article = articleDAO.findById(articleId);

            if (article == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Bài viết không tồn tại");
                return;
            }

            // Tăng lượt xem
            articleDAO.incrementViews(articleId);

            // Lấy danh sách category cho menu
            List<Category> categories = categoryDAO.findAll();

            request.setAttribute("article", article);
            request.setAttribute("categories", categories);

            request.getRequestDispatcher("/WEB-INF/views/web/article-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Hiển thị bài viết theo danh mục
    private void showArticlesByCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int categoryId = Integer.parseInt(request.getParameter("id"));

            // Lấy thông tin category
            Category category = categoryDAO.findById(categoryId);

            if (category == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Danh mục không tồn tại");
                return;
            }

            // Lấy bài viết thuộc category này
            List<Article> articles = articleDAO.findByCategory(categoryId);

            // Lấy tất cả category cho menu
            List<Category> categories = categoryDAO.findAll();

            request.setAttribute("currentCategory", category);
            request.setAttribute("articles", articles);
            request.setAttribute("categories", categories);

            request.getRequestDispatcher("/WEB-INF/views/web/category.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
        }
    }

    // Tìm kiếm bài viết
    private void handleSearch(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("keyword");

        if (keyword == null || keyword.trim().isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        // Tìm kiếm bài viết
        List<Article> articles = articleDAO.search(keyword.trim());

        // Lấy category cho menu
        List<Category> categories = categoryDAO.findAll();

        request.setAttribute("keyword", keyword);
        request.setAttribute("articles", articles);
        request.setAttribute("categories", categories);

        request.getRequestDispatcher("/WEB-INF/views/web/search-results.jsp").forward(request, response);
    }
}