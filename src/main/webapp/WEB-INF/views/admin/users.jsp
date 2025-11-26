<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản Lý User</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="/WEB-INF/views/admin/includes/sidebar.jsp"/>

    <!-- Main -->
    <div class="flex-grow-1 p-4 bg-light">
        <h2 class="mb-4">Quản Lý User</h2>

        <c:if test="${param.msg == 'blocked'}">
            <div class="alert alert-success">Đã khóa user.</div>
        </c:if>
        <c:if test="${param.msg == 'unblocked'}">
            <div class="alert alert-success">Đã mở khóa user.</div>
        </c:if>
        <c:if test="${param.error == 'block_failed'}">
            <div class="alert alert-danger">Khóa user thất bại.</div>
        </c:if>
        <c:if test="${param.error == 'unblock_failed'}">
            <div class="alert alert-danger">Mở khóa user thất bại.</div>
        </c:if>

        <div class="card shadow-sm">
            <div class="card-body">
                <table class="table table-hover align-middle">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Username</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Trạng thái</th>
                        <th>Ngày tạo</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty users}">
                        <tr>
                            <td colspan="7" class="text-center text-muted">Không có user nào.</td>
                        </tr>
                    </c:if>

                    <c:forEach var="u" items="${users}" varStatus="st">
                        <tr>
                            <td>${st.index + 1}</td>
                            <td>${u.username}</td>
                            <td>${u.email}</td>
                            <td>${u.role}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.status == 'ACTIVE'}">
                                        <span class="badge bg-success">ACTIVE</span>
                                    </c:when>
                                    <c:when test="${u.status == 'BLOCKED'}">
                                        <span class="badge bg-danger">BLOCKED</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary">${u.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${u.createdAt}</td>
                            <td>
                                <c:if test="${u.role != 'ADMIN'}">
                                    <c:choose>
                                        <c:when test="${u.status == 'ACTIVE'}">
                                            <form action="${pageContext.request.contextPath}/admin/block-user"
                                                  method="post" class="d-inline">
                                                <input type="hidden" name="id" value="${u.id}">
                                                <button type="submit" class="btn btn-sm btn-danger"
                                                        onclick="return confirm('Khóa user này?');">
                                                    <i class="bi bi-lock"></i> Khóa
                                                </button>
                                            </form>
                                        </c:when>
                                        <c:when test="${u.status == 'BLOCKED'}">
                                            <form action="${pageContext.request.contextPath}/admin/unblock-user"
                                                  method="post" class="d-inline">
                                                <input type="hidden" name="id" value="${u.id}">
                                                <button type="submit" class="btn btn-sm btn-success">
                                                    <i class="bi bi-unlock"></i> Mở khóa
                                                </button>
                                            </form>
                                        </c:when>
                                    </c:choose>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="mt-3">
            <a href="${pageContext.request.contextPath}/admin/cleanup-users" class="btn btn-outline-secondary">
                <i class="bi bi-broom"></i> Dọn dẹp user không hoạt động
            </a>
        </div>

    </div>
</div>
</body>
</html>
