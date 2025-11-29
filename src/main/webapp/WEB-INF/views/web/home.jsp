<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang Chủ - Báo Điện Tử</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    <style>
        .article-card img { height: 200px; object-fit: cover; }
        .article-card:hover { transform: translateY(-5px); transition: 0.3s; box-shadow: 0 4px 15px rgba(0,0,0,0.1); }
        .navbar-nav .nav-link { padding: 0.5rem 1rem; }
        .navbar-nav .nav-link.active { background-color: rgba(255,255,255,0.2); border-radius: 5px; }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">
            <i class="bi bi-newspaper"></i> DAILY NEWS
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav me-auto">
                <%-- Tab "Tất cả" --%>
                <li class="nav-item">
                    <a class="nav-link ${empty param.category ? 'active' : ''}"
                       href="${pageContext.request.contextPath}/home">
                        <i class="bi bi-house-door"></i> Tất cả
                    </a>
                </li>

                <%-- Lặp qua tất cả categories từ database --%>
                <c:forEach var="cat" items="${categories}" varStatus="status">
                    <%-- Chỉ hiển thị tối đa 5 categories trên navbar --%>
                    <c:if test="${status.index < 5}">
                        <li class="nav-item">
                            <a class="nav-link ${param.category == cat.id ? 'active' : ''}"
                               href="${pageContext.request.contextPath}/home?category=${cat.id}">
                                    ${cat.name}
                            </a>
                        </li>
                    </c:if>
                </c:forEach>

                <%-- Nếu có nhiều hơn 5 categories, hiển thị dropdown "Xem thêm" --%>
                <c:if test="${categories.size() > 5}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            Xem thêm
                        </a>
                        <ul class="dropdown-menu">
                            <c:forEach var="cat" items="${categories}" varStatus="status">
                                <c:if test="${status.index >= 5}">
                                    <li>
                                        <a class="dropdown-item"
                                           href="${pageContext.request.contextPath}/home?category=${cat.id}">
                                                ${cat.name}
                                        </a>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </li>
                </c:if>
            </ul>

            <!-- Phần kiểm tra đăng nhập -->
            <div class="d-flex align-items-center">
                <c:choose>
                    <%-- Nếu session có 'currentUser' -> Đã đăng nhập --%>
                    <c:when test="${not empty sessionScope.currentUser}">
                        <div class="dropdown">
                            <button class="btn btn-light dropdown-toggle" type="button" data-bs-toggle="dropdown">
                                Xin chào, <strong>${sessionScope.currentUser.fullName}</strong>
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/my-articles">
                                    <i class="bi bi-pen"></i> Quản lý bài viết
                                </a></li>

                                    <%-- Nếu là ADMIN thì hiện thêm link vào trang quản trị --%>
                                <c:if test="${sessionScope.currentUser.role == 'ADMIN'}">
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/admin">
                                        <i class="bi bi-speedometer2"></i> Trang Admin
                                    </a></li>
                                </c:if>

                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
                            </ul>
                        </div>
                    </c:when>
                    <%-- Nếu chưa đăng nhập --%>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light me-2">Đăng nhập</a>
                        <a href="${pageContext.request.contextPath}/register" class="btn btn-warning">Đăng ký</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>

<!-- Main Content -->
<div class="container mt-5">
    <div class="row">
        <!-- Danh sách bài viết -->
        <div class="col-md-9">
            <%-- Tiêu đề động dựa vào category --%>
            <h3 class="border-bottom pb-2 mb-4 text-primary">
                <c:choose>
                    <c:when test="${not empty currentCategory}">
                        <i class="bi bi-tag"></i> ${currentCategory.name}
                    </c:when>
                    <c:otherwise>
                        <i class="bi bi-newspaper"></i> Tin Mới Nhất
                    </c:otherwise>
                </c:choose>
            </h3>

            <c:if test="${empty articles}">
                <div class="alert alert-info">Chưa có bài viết nào.</div>
            </c:if>

            <div class="row">
                <c:forEach var="a" items="${articles}">
                    <div class="col-md-4 mb-4">
                        <div class="card article-card h-100 border-0 shadow-sm">
                            <img src="${not empty a.thumbnail ? pageContext.request.contextPath.concat('/images/').concat(a.thumbnail) : 'https://via.placeholder.com/300x200'}"
                                 class="card-img-top"
                                 alt="${a.title}"
                                 onerror="this.src='https://via.placeholder.com/300x200'">

                            <div class="card-body">
                                <span class="badge bg-secondary mb-2">${a.categoryName}</span>
                                <h5 class="card-title">
                                    <a href="${pageContext.request.contextPath}/article-detail?id=${a.id}"
                                       class="text-decoration-none text-dark">${a.title}</a>
                                </h5>
                                <p class="card-text text-muted small">${a.shortDescription}</p>
                            </div>
                            <div class="card-footer bg-white border-0 d-flex justify-content-between small text-muted">
                                <span><i class="bi bi-person"></i> ${a.authorName}</span>
                                <span><i class="bi bi-eye"></i> ${a.views}</span>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>

        <!-- Sidebar bên phải -->
        <div class="col-md-3">
            <%-- Tin nổi bật từ database --%>
            <div class="card mb-3">
                <div class="card-header bg-primary text-white">
                    <i class="bi bi-fire"></i> Tin Nổi Bật
                </div>
                <ul class="list-group list-group-flush">
                    <c:choose>
                        <c:when test="${not empty featuredArticles}">
                            <c:forEach var="fa" items="${featuredArticles}" varStatus="status">
                                <li class="list-group-item">
                                    <a href="${pageContext.request.contextPath}/article-detail?id=${fa.id}"
                                       class="text-decoration-none d-flex align-items-start">
                                        <span class="badge bg-danger me-2">${status.index + 1}</span>
                                        <span class="small">${fa.title}</span>
                                    </a>
                                </li>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <li class="list-group-item">
                                <small class="text-muted">Chưa có tin nổi bật</small>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>

            <%-- Danh mục --%>
            <div class="card mb-3">
                <div class="card-header bg-secondary text-white">
                    <i class="bi bi-list"></i> Danh Mục
                </div>
                <ul class="list-group list-group-flush">
                    <c:forEach var="cat" items="${categories}">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <a href="${pageContext.request.contextPath}/home?category=${cat.id}"
                               class="text-decoration-none ${param.category == cat.id ? 'fw-bold text-primary' : ''}">
                                    ${cat.name}
                            </a>
                                <%-- Lấy count từ Map --%>
                            <c:set var="count" value="${categoryCount[cat.id]}" />
                            <span class="badge bg-primary rounded-pill">${count != null ? count : 0}</span>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>