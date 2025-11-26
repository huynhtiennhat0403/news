<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="bg-dark text-white p-3 vh-100" style="width: 250px;">
    <h4>ADMIN PANEL</h4>
    <hr>
    <ul class="nav flex-column">
        <li class="nav-item mb-2">
            <a href="${pageContext.request.contextPath}/admin"
               class="nav-link text-white ${activePage == 'dashboard' ? 'bg-primary' : ''}">
                <i class="bi bi-speedometer2"></i> Dashboard
            </a>
        </li>

        <li class="nav-item mb-2">
            <a href="${pageContext.request.contextPath}/admin/articles"
               class="nav-link text-white ${activePage == 'articles' ? 'bg-primary' : ''}">
                <i class="bi bi-file-text"></i> Duyệt Bài Viết
            </a>
        </li>

        <li class="nav-item mb-2">
            <a href="${pageContext.request.contextPath}/admin/categories"
               class="nav-link text-white ${activePage == 'categories' ? 'bg-primary' : ''}">
                <i class="bi bi-tags"></i> Quản Lý Danh Mục
            </a>
        </li>

        <li class="nav-item mb-2">
            <a href="${pageContext.request.contextPath}/admin/users"
               class="nav-link text-white ${activePage == 'users' ? 'bg-primary' : ''}">
                <i class="bi bi-people"></i> Quản Lý User
            </a>
        </li>

        <li class="nav-item mb-2">
            <a href="${pageContext.request.contextPath}/admin/remove-requests"
               class="nav-link text-white ${activePage == 'remove-requests' ? 'bg-primary' : ''}">
                <i class="bi bi-flag"></i> Yêu Cầu Gỡ Bài
            </a>
        </li>

        <li class="nav-item mt-5">
            <a href="${pageContext.request.contextPath}/home" class="nav-link text-warning">
                <i class="bi bi-arrow-left"></i> Về Trang Chủ
            </a>
        </li>
    </ul>
</div>
