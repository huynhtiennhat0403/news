package com.example.app.utils;

import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

public class Upload {

    private static final String UPLOAD_DIR = getUploadDirectory();

    private static String getUploadDirectory() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return "D:/uploads";  // Windows
        } else {
            return "/var/www/uploads";  // Linux/Mac
        }
    }

    public static String saveFile(Part part, String realPath) {
        return saveFile(part);
    }

    public static String saveFile(Part part) {
        try {
            if (part == null || part.getSize() == 0) {
                return null;
            }

            String fileName = Paths.get(part.getSubmittedFileName()).getFileName().toString();
            if (fileName == null || fileName.isEmpty()) {
                return null;
            }

            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (created) {
                    System.out.println("✅ Đã tạo thư mục: " + UPLOAD_DIR);
                }
            }

            String fullPath = UPLOAD_DIR + File.separator + uniqueFileName;
            part.write(fullPath);

            System.out.println("✅ Đã lưu file: " + fullPath);

            return uniqueFileName;

        } catch (IOException e) {
            System.err.println("❌ Lỗi upload file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        File file = new File(UPLOAD_DIR + File.separator + fileName);
        if (file.exists() && file.isFile()) {
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("✅ Đã xóa file: " + fileName);
            }
            return deleted;
        }
        return false;
    }
}