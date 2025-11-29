<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${article.title}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">

<div class="container mt-4">

    <!-- Thanh điều hướng nhỏ -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <div>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary btn-sm">
                <i class="bi bi-house"></i> Trang chủ
            </a>
        </div>

        <div>
            <a href="${pageContext.request.contextPath}/my-articles" class="btn btn-outline-primary btn-sm">
                <i class="bi bi-journal-text"></i> Bài viết của tôi
            </a>
        </div>
    </div>

    <c:if test="${article == null}">
        <div class="alert alert-danger">
            Không tìm thấy bài viết.
        </div>
    </c:if>

    <c:if test="${article != null}">
        <div class="row">
            <div class="col-lg-9">

                <!-- Thông tin chính -->
                <div class="card shadow-sm mb-4">
                    <div class="card-body">

                        <!-- Tiêu đề -->
                        <h1 class="h3 mb-3">${article.title}</h1>

                        <!-- Meta: chuyên mục, tác giả, ngày tạo, lượt xem -->
                        <div class="mb-3 text-muted small">
                            <span class="badge bg-primary me-2">
                                    ${article.categoryName}
                            </span>

                            <i class="bi bi-person"></i>
                                ${article.authorName}

                            &nbsp; | &nbsp;
                            <i class="bi bi-calendar-event"></i>
                                ${article.createdAt}

                            &nbsp; | &nbsp;
                            <i class="bi bi-eye"></i>
                                ${article.views} lượt xem
                        </div>

                        <!-- Trạng thái bài (nếu không phải guest hoặc bạn muốn hiển thị luôn) -->
                        <div class="mb-3">
                            <c:choose>
                                <c:when test="${article.status == 'PUBLISHED'}">
                                    <span class="badge bg-success">Đã đăng</span>
                                </c:when>
                                <c:when test="${article.status == 'PENDING'}">
                                    <span class="badge bg-warning text-dark">Đang chờ duyệt</span>
                                </c:when>
                                <c:when test="${article.status == 'DRAFT'}">
                                    <span class="badge bg-secondary">Nháp</span>
                                </c:when>
                                <c:when test="${article.status == 'REJECTED'}">
                                    <span class="badge bg-danger">Bị từ chối</span>
                                </c:when>
                                <c:when test="${article.status == 'REMOVE_PENDING'}">
                                    <span class="badge bg-dark">Đang chờ gỡ</span>
                                </c:when>
                            </c:choose>
                        </div>

                        <!-- Lý do từ chối / admin message (nếu có) -->
                        <c:if test="${not empty article.adminMessage}">
                            <div class="alert alert-warning py-2 small">
                                <i class="bi bi-exclamation-triangle"></i>
                                <strong>Thông báo từ Admin:</strong> ${article.adminMessage}
                            </div>
                        </c:if>

                        <!-- ✅ Thumbnail - ĐÃ SỬA -->
                        <c:if test="${not empty article.thumbnail}">
                            <div class="mb-3 text-center">
                                <img src="${pageContext.request.contextPath}/images/${article.thumbnail}"
                                     alt="${article.title}"
                                     class="img-fluid rounded"
                                     style="max-height: 400px; object-fit: cover;"
                                     onerror="this.src='https://via.placeholder.com/800x400'">
                            </div>
                        </c:if>

                        <!-- Nội dung bài viết -->
                        <div class="mt-4">
                            <!-- Nếu content có chứa HTML, nên escapeXml="false" -->
                            <c:out value="${article.content}" escapeXml="false"/>
                        </div>
                    </div>
                </div>

            </div>

            <!-- Cột phải: mô tả ngắn / hộp thông tin -->
            <div class="col-lg-3">

                <div class="card shadow-sm mb-3">
                    <div class="card-header">
                        <strong>Tóm tắt</strong>
                    </div>
                    <div class="card-body">
                        <p class="small mb-0">${article.shortDescription}</p>
                    </div>
                </div>

                <c:if test="${not empty article.userMessage}">
                    <div class="card shadow-sm mb-3">
                        <div class="card-header">
                            <strong>Lý do yêu cầu gỡ (User)</strong>
                        </div>
                        <div class="card-body">
                            <p class="small mb-0">${article.userMessage}</p>
                        </div>
                    </div>
                </c:if>

            </div>
        </div>
    </c:if>

</div>

</body>
</html>