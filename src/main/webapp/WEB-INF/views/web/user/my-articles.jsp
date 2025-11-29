<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bài Viết Của Tôi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        .rejection-reason {
            background-color: #fff3cd;
            border-left: 4px solid #dc3545;
            padding: 8px 12px;
            margin-top: 8px;
            border-radius: 4px;
            font-size: 0.875rem;
        }
        .article-row:hover {
            background-color: #f8f9fa;
        }
    </style>
</head>
<body class="bg-light">

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="bi bi-journal-text"></i> Quản Lý Bài Viết</h2>
        <div>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary me-2">Về trang chủ</a>
            <a href="${pageContext.request.contextPath}/write-article" class="btn btn-primary">
                <i class="bi bi-plus-lg"></i> Viết bài mới
            </a>
        </div>
    </div>

    <!-- Hiển thị thông báo -->
    <c:if test="${not empty param.msg}">
        <div class="alert alert-success alert-dismissible fade show">
            <i class="bi bi-check-circle"></i>
            <c:choose>
                <c:when test="${param.msg == 'success'}">Đã lưu bài viết thành công!</c:when>
                <c:when test="${param.msg == 'updated'}">Đã cập nhật bài viết!</c:when>
                <c:when test="${param.msg == 'deleted'}">Đã xóa bài viết!</c:when>
                <c:when test="${param.msg == 'remove_requested'}">Đã gửi yêu cầu gỡ bài!</c:when>
                <c:otherwise>${param.msg}</c:otherwise>
            </c:choose>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show">
            <i class="bi bi-exclamation-triangle"></i>
            <c:choose>
                <c:when test="${param.error == 'cannot_delete'}">Không thể xóa bài viết đã PUBLISHED!</c:when>
                <c:when test="${param.error == 'cannot_edit'}">Không thể sửa bài viết đã PUBLISHED!</c:when>
                <c:when test="${param.error == 'delete_failed'}">Xóa bài viết thất bại!</c:when>
                <c:when test="${param.error == 'update_failed'}">Cập nhật thất bại!</c:when>
                <c:when test="${param.error == 'not_published'}">Bài viết chưa được đăng!</c:when>
                <c:when test="${param.error == 'request_failed'}">Gửi yêu cầu thất bại!</c:when>
                <c:otherwise>${param.error}</c:otherwise>
            </c:choose>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-body">
            <table class="table table-hover align-middle">
                <thead class="table-light">
                <tr>
                    <th style="width: 5%;">#</th>
                    <th style="width: 30%;">Tiêu đề</th>
                    <th style="width: 15%;">Chuyên mục</th>
                    <th style="width: 20%;">Trạng thái</th>
                    <th style="width: 30%;">Thao tác</th>
                </tr>
                </thead>

                <tbody>

                <!-- Nếu không có bài viết -->
                <c:if test="${empty myArticles}">
                    <tr>
                        <td colspan="5" class="text-center text-muted py-5">
                            <i class="bi bi-inbox" style="font-size: 3rem;"></i>
                            <p class="mt-2">Chưa có bài viết nào.</p>
                            <a href="${pageContext.request.contextPath}/write-article" class="btn btn-primary">
                                <i class="bi bi-plus-lg"></i> Viết bài đầu tiên
                            </a>
                        </td>
                    </tr>
                </c:if>

                <!-- Render danh sách bài viết -->
                <c:forEach var="a" items="${myArticles}" varStatus="loop">

                    <tr class="article-row">
                        <td>${loop.index + 1}</td>

                        <!-- Tiêu đề -->
                        <td>
                            <strong>${a.title}</strong>
                            <br>
                            <small class="text-muted">
                                <i class="bi bi-calendar3"></i> ${a.createdAt}
                            </small>
                        </td>

                        <!-- Chuyên mục -->
                        <td>
                            <span class="badge bg-secondary">${a.categoryName}</span>
                        </td>

                        <!-- Trạng thái -->
                        <td>
                            <c:choose>
                                <c:when test="${a.status == 'DRAFT'}">
                                    <span class="badge bg-secondary">
                                        <i class="bi bi-file-earmark"></i> Nháp
                                    </span>
                                    <div class="small text-muted mt-1">
                                        Bài viết chưa được gửi duyệt
                                    </div>
                                </c:when>

                                <c:when test="${a.status == 'PENDING'}">
                                    <span class="badge bg-warning text-dark">
                                        <i class="bi bi-clock-history"></i> Chờ duyệt
                                    </span>
                                    <div class="small text-muted mt-1">
                                        Đang chờ Admin xét duyệt
                                    </div>
                                </c:when>

                                <c:when test="${a.status == 'PUBLISHED'}">
                                    <span class="badge bg-success">
                                        <i class="bi bi-check-circle"></i> Đã đăng
                                    </span>
                                    <div class="small text-success mt-1">
                                        <i class="bi bi-eye"></i> ${a.views} lượt xem
                                    </div>
                                </c:when>

                                <c:when test="${a.status == 'REJECTED'}">
                                    <span class="badge bg-danger">
                                        <i class="bi bi-x-circle"></i> Bị từ chối
                                    </span>
                                    <c:if test="${not empty a.adminMessage}">
                                        <div class="rejection-reason">
                                            <i class="bi bi-info-circle"></i>
                                            <strong>Lý do:</strong> ${a.adminMessage}
                                        </div>
                                    </c:if>
                                </c:when>

                                <c:when test="${a.status == 'REMOVE_PENDING'}">
                                    <span class="badge bg-dark">
                                        <i class="bi bi-hourglass-split"></i> Chờ gỡ
                                    </span>
                                    <div class="small text-muted mt-1">
                                        Đang chờ Admin xử lý yêu cầu gỡ
                                    </div>
                                </c:when>
                            </c:choose>
                        </td>

                        <!-- Các thao tác -->
                        <td>
                            <!-- Xem bài -->
                            <a href="${pageContext.request.contextPath}/article-detail?id=${a.id}"
                               class="btn btn-sm btn-info text-white mb-1"
                               title="Xem chi tiết">
                                <i class="bi bi-eye"></i>
                            </a>

                            <!-- Nếu có quyền sửa (DRAFT, PENDING, REJECTED) -->
                            <c:if test="${a.status == 'DRAFT' || a.status == 'PENDING' || a.status == 'REJECTED'}">
                                <a href="${pageContext.request.contextPath}/edit-article?id=${a.id}"
                                   class="btn btn-sm btn-warning mb-1"
                                   title="Chỉnh sửa">
                                    <i class="bi bi-pencil"></i>
                                </a>

                                <a href="${pageContext.request.contextPath}/delete-article?id=${a.id}"
                                   onclick="return confirm('Bạn chắc chắn muốn xóa bài viết \"${a.title}\"?')"
                                class="btn btn-sm btn-danger mb-1"
                                title="Xóa">
                                <i class="bi bi-trash"></i>
                                </a>
                            </c:if>

                            <!-- Nếu PUBLISHED thì có thể yêu cầu gỡ -->
                            <c:if test="${a.status == 'PUBLISHED'}">
                                <a href="${pageContext.request.contextPath}/request-remove?id=${a.id}"
                                   class="btn btn-sm btn-secondary mb-1"
                                   title="Yêu cầu gỡ bài">
                                    <i class="bi bi-flag"></i> Yêu cầu gỡ
                                </a>
                            </c:if>
                        </td>

                    </tr>

                </c:forEach>

                </tbody>

            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>