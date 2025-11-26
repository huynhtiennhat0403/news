<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sửa Danh Mục</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="/WEB-INF/views/admin/includes/sidebar.jsp"/>

    <!-- Main -->
    <div class="flex-grow-1 p-4 bg-light">
        <h2 class="mb-4">Sửa Danh Mục</h2>

        <c:if test="${category == null}">
            <div class="alert alert-danger">Danh mục không tồn tại.</div>
            <a href="${pageContext.request.contextPath}/admin/categories" class="btn btn-secondary">Quay lại</a>
        </c:if>

        <c:if test="${category != null}">
            <div class="card shadow-sm">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/admin/edit-category" method="post" class="row g-3">
                        <input type="hidden" name="id" value="${category.id}">
                        <div class="col-md-6">
                            <label class="form-label">Tên danh mục</label>
                            <input type="text" name="name" class="form-control"
                                   value="${category.name}" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Mô tả</label>
                            <input type="text" name="description" class="form-control"
                                   value="${category.description}">
                        </div>

                        <div class="col-12 d-flex justify-content-between mt-3">
                            <a href="${pageContext.request.contextPath}/admin/categories"
                               class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left"></i> Hủy
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-save"></i> Lưu thay đổi
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </c:if>

    </div>
</div>
</body>
</html>
