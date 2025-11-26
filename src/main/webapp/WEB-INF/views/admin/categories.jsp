<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản Lý Danh Mục</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="/WEB-INF/views/admin/includes/sidebar.jsp"/>

    <!-- Main -->
    <div class="flex-grow-1 p-4 bg-light">
        <h2 class="mb-4">Quản Lý Danh Mục</h2>

        <c:if test="${param.msg == 'added'}">
            <div class="alert alert-success">Đã thêm danh mục.</div>
        </c:if>
        <c:if test="${param.msg == 'updated'}">
            <div class="alert alert-success">Đã cập nhật danh mục.</div>
        </c:if>
        <c:if test="${param.msg == 'deleted'}">
            <div class="alert alert-success">Đã xóa danh mục.</div>
        </c:if>
        <c:if test="${param.error == 'add_failed'}">
            <div class="alert alert-danger">Thêm danh mục thất bại.</div>
        </c:if>
        <c:if test="${param.error == 'update_failed'}">
            <div class="alert alert-danger">Cập nhật danh mục thất bại.</div>
        </c:if>
        <c:if test="${param.error == 'has_articles'}">
            <div class="alert alert-warning">Không thể xóa, danh mục đang có bài viết.</div>
        </c:if>

        <!-- Form thêm mới -->
        <div class="card mb-4 shadow-sm">
            <div class="card-header">Thêm danh mục mới</div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/admin/add-category" method="post" class="row g-3">
                    <div class="col-md-4">
                        <label class="form-label">Tên danh mục</label>
                        <input type="text" name="name" class="form-control" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label">Mô tả</label>
                        <input type="text" name="description" class="form-control">
                    </div>
                    <div class="col-md-2 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary w-100">
                            <i class="bi bi-plus-lg"></i> Thêm
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <!-- Danh sách category -->
        <div class="card shadow-sm">
            <div class="card-body">
                <table class="table table-hover align-middle">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Tên danh mục</th>
                        <th>Mô tả</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty categories}">
                        <tr>
                            <td colspan="4" class="text-center text-muted">Chưa có danh mục nào.</td>
                        </tr>
                    </c:if>

                    <c:forEach var="c" items="${categories}" varStatus="st">
                        <tr>
                            <td>${st.index + 1}</td>
                            <td>${c.name}</td>
                            <td>${c.description}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/edit-category?id=${c.id}"
                                   class="btn btn-sm btn-warning">
                                    <i class="bi bi-pencil"></i> Sửa
                                </a>

                                <form action="${pageContext.request.contextPath}/admin/delete-category"
                                      method="post" class="d-inline">
                                    <input type="hidden" name="id" value="${c.id}">
                                    <button type="submit" class="btn btn-sm btn-danger"
                                            onclick="return confirm('Xóa danh mục này?');">
                                        <i class="bi bi-trash"></i> Xóa
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

    </div>
</div>
</body>
</html>
