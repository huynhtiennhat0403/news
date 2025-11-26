<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<div class="d-flex">
    <!-- Sidebar dùng chung -->
    <jsp:include page="/WEB-INF/views/admin/includes/sidebar.jsp"/>

    <!-- Main Content -->
    <div class="flex-grow-1 p-4 bg-light">
        <h2 class="mb-4">Tổng Quan Hệ Thống</h2>

        <div class="row">
            <div class="col-md-3">
                <div class="card text-white bg-primary mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Tổng Bài Viết</h5>
                        <h2 class="card-text">${totalArticles}</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white bg-success mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Tổng User</h5>
                        <h2 class="card-text">${totalUsers}</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white bg-warning mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Bài Chờ Duyệt</h5>
                        <h2 class="card-text">${pendingArticles}</h2>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card text-white bg-danger mb-3">
                    <div class="card-body">
                        <h5 class="card-title">Yêu Cầu Gỡ Bài</h5>
                        <h2 class="card-text">${removeRequests}</h2>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bạn có thể thêm bảng thống kê khác tại đây nếu muốn -->
    </div>
</div>
</body>
</html>
