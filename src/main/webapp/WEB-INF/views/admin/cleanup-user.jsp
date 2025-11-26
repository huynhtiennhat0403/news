<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Dọn Dẹp User</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="/WEB-INF/views/admin/includes/sidebar.jsp"/>

    <!-- Main -->
    <div class="flex-grow-1 p-4 bg-light">
        <h2 class="mb-4">Dọn Dẹp User Không Hoạt Động (≥ 2 tháng)</h2>

        <c:if test="${param.error == 'no_selection'}">
            <div class="alert alert-warning">Bạn chưa chọn user nào để xóa.</div>
        </c:if>
        <c:if test="${param.msg == 'deleted'}">
            <div class="alert alert-success">
                Đã xóa ${param.count} user không hoạt động.
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/cleanup-users" method="post">
            <div class="card shadow-sm">
                <div class="card-body">
                    <table class="table table-hover align-middle">
                        <thead>
                        <tr>
                            <th><input type="checkbox" onclick="
                                const cbs = document.querySelectorAll('.user-check');
                                cbs.forEach(cb => cb.checked = this.checked);
                            "></th>
                            <th>#</th>
                            <th>Username</th>
                            <th>Email</th>
                            <th>Trạng thái</th>
                            <th>Lần đăng nhập cuối</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty inactiveUsers}">
                            <tr>
                                <td colspan="6" class="text-center text-muted">
                                    Không có user nào cần dọn dẹp.
                                </td>
                            </tr>
                        </c:if>

                        <c:forEach var="u" items="${inactiveUsers}" varStatus="st">
                            <tr>
                                <td>
                                    <input type="checkbox" class="user-check" name="userIds" value="${u.id}">
                                </td>
                                <td>${st.index + 1}</td>
                                <td>${u.username}</td>
                                <td>${u.email}</td>
                                <td>${u.status}</td>
                                <td>${u.lastLoginAt}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="mt-3 d-flex justify-content-between">
                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline-secondary">
                    <i class="bi bi-arrow-left"></i> Quay lại
                </a>
                <button type="submit" class="btn btn-danger"
                        onclick="return confirm('Xác nhận xóa các user đã chọn?');">
                    <i class="bi bi-trash"></i> Xóa user đã chọn
                </button>
            </div>
        </form>

    </div>
</div>
</body>
</html>
