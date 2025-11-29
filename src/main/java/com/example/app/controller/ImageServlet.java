package com.example.app.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {

    private static final String UPLOAD_DIR = getUploadDirectory();

    private static String getUploadDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "D:/uploads";
        } else {
            return "/var/www/uploads";
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy tên file từ URL: /images/abc-123.jpg -> abc-123.jpg
        String filename = request.getPathInfo();

        if (filename == null || filename.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tên file");
            return;
        }

        // Bỏ dấu "/" ở đầu
        filename = filename.substring(1);

        // Đường dẫn đầy đủ đến file
        File file = new File(UPLOAD_DIR + File.separator + filename);

        // Kiểm tra file có tồn tại không
        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy ảnh");
            return;
        }

        // Kiểm tra bảo mật: file phải nằm trong UPLOAD_DIR
        String canonicalPath = file.getCanonicalPath();
        String canonicalUploadPath = new File(UPLOAD_DIR).getCanonicalPath();

        if (!canonicalPath.startsWith(canonicalUploadPath)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Truy cập bị từ chối");
            return;
        }

        // Set Content-Type dựa vào extension
        String contentType = getServletContext().getMimeType(file.getName());
        if (contentType == null) {
            contentType = "image/jpeg"; // Default
        }

        response.setContentType(contentType);
        response.setContentLength((int) file.length());

        // Cache ảnh 1 ngày để tăng performance
        response.setHeader("Cache-Control", "public, max-age=86400");
        response.setDateHeader("Expires", System.currentTimeMillis() + 86400000L);

        // Đọc file và ghi ra response
        try (FileInputStream in = new FileInputStream(file);
                OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("❌ Lỗi đọc file: " + e.getMessage());
            throw e;
        }
    }
}