<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản Lý Bài Viết</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<div class="d-flex">
    <!-- Sidebar  -->
    <jsp:include page="/WEB-INF/views/admin/includes/sidebar.jsp"/>

    <!-- Main -->
    <div class="flex-grow-1 p-4 bg-light">
        <h2 class="mb-4">Quản Lý Bài Viết</h2>

        <!-- Thông báo -->
        <c:if test="${param.msg == 'approved'}">
            <div class="alert alert-success">Đã duyệt bài viết.</div>
        </c:if>
        <c:if test="${param.msg == 'rejected'}">
            <div class="alert alert-success">Đã từ chối bài viết.</div>
        </c:if>
        <c:if test="${param.error == 'approve_failed'}">
            <div class="alert alert-danger">Duyệt bài thất bại.</div>
        </c:if>
        <c:if test="${param.error == 'reject_failed'}">
            <div class="alert alert-danger">Từ chối bài thất bại.</div>
        </c:if>
        <c:if test="${param.error == 'reason_required'}">
            <div class="alert alert-warning">Vui lòng nhập lý do từ chối.</div>
        </c:if>

        <div class="card shadow-sm">
            <div class="card-body">
                <table class="table table-hover align-middle">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Tiêu đề</th>
                        <th>Tác giả</th>
                        <th>Chuyên mục</th>
                        <th>Trạng thái</th>
                        <th>Ngày tạo</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty articles}">
                        <tr>
                            <td colspan="7" class="text-center text-muted">Không có bài viết nào.</td>
                        </tr>
                    </c:if>

                    <c:forEach var="a" items="${articles}" varStatus="st">
                        <tr>
                            <td>${st.index + 1}</td>
                            <td>${a.title}</td>
                            <td>${a.authorName}</td>
                            <td>${a.categoryName}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${a.status == 'PENDING'}">
                                        <span class="badge bg-warning text-dark">PENDING</span>
                                    </c:when>
                                    <c:when test="${a.status == 'PUBLISHED'}">
                                        <span class="badge bg-success">PUBLISHED</span>
                                    </c:when>
                                    <c:when test="${a.status == 'REJECTED'}">
                                        <span class="badge bg-danger">REJECTED</span>
                                    </c:when>
                                    <c:when test="${a.status == 'DRAFT'}">
                                        <span class="badge bg-secondary">DRAFT</span>
                                    </c:when>
                                    <c:when test="${a.status == 'REMOVE_PENDING'}">
                                        <span class="badge bg-dark">REMOVE_PENDING</span>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>${a.createdAt}</td>
                            <td>
                                <!-- Xem bài -->
                                <a href="${pageContext.request.contextPath}/article-detail?id=${a.id}"
                                   class="btn btn-sm btn-info text-white mb-1">
                                    <i class="bi bi-eye"></i>
                                </a>

                                <!-- Duyệt / Từ chối chỉ cho PENDING -->
                                <c:if test="${a.status == 'PENDING'}">
                                    <!-- Approve -->
                                    <form action="${pageContext.request.contextPath}/admin/approve"
                                          method="post" class="d-inline">
                                        <input type="hidden" name="id" value="${a.id}">
                                        <button type="submit" class="btn btn-sm btn-success mb-1">
                                            <i class="bi bi-check"></i>
                                        </button>
                                    </form>

                                    <!-- Reject -->
                                    <form action="${pageContext.request.contextPath}/admin/reject"
                                          method="post" class="d-inline">
                                        <input type="hidden" name="id" value="${a.id}">
                                        <input type="text" name="reason" class="form-control form-control-sm d-inline-block"
                                               style="width: 150px;" placeholder="Lý do..." required>
                                        <button type="submit" class="btn btn-sm btn-danger mb-1">
                                            <i class="bi bi-x"></i>
                                        </button>
                                    </form>
                                </c:if>

                                <!-- Hiển thị lý do reject (nếu có) -->
                                <c:if test="${a.status == 'REJECTED' && not empty a.adminMessage}">
                                    <div class="small text-danger mt-1">
                                        Lý do: ${a.adminMessage}
                                    </div>
                                </c:if>
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
