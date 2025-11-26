<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Yêu Cầu Gỡ Bài</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body>
<div class="d-flex">
    <!-- Sidebar -->
    <jsp:include page="/WEB-INF/views/admin/includes/sidebar.jsp"/>

    <!-- Main -->
    <div class="flex-grow-1 p-4 bg-light">
        <h2 class="mb-4">Yêu Cầu Gỡ Bài</h2>

        <c:if test="${param.msg == 'handled'}">
            <div class="alert alert-success">Đã xử lý yêu cầu.</div>
        </c:if>
        <c:if test="${param.error == 'handle_failed'}">
            <div class="alert alert-danger">Xử lý yêu cầu thất bại.</div>
        </c:if>

        <div class="card shadow-sm">
            <div class="card-body">
                <table class="table table-hover align-middle">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Tiêu đề</th>
                        <th>Tác giả</th>
                        <th>Lý do user yêu cầu gỡ</th>
                        <th>Ngày cập nhật</th>
                        <th>Thao tác</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty removeRequests}">
                        <tr>
                            <td colspan="6" class="text-center text-muted">Không có yêu cầu gỡ bài nào.</td>
                        </tr>
                    </c:if>

                    <c:forEach var="a" items="${removeRequests}" varStatus="st">
                        <tr>
                            <td>${st.index + 1}</td>
                            <td>${a.title}</td>
                            <td>${a.authorName}</td>
                            <td>${a.userMessage}</td>
                            <td>${a.updatedAt}</td>
                            <td>
                                <!-- Duyệt gỡ (xóa bài) -->
                                <form action="${pageContext.request.contextPath}/admin/handle-remove"
                                      method="post" class="mb-1">
                                    <input type="hidden" name="id" value="${a.id}">
                                    <input type="hidden" name="action" value="approve">
                                    <button type="submit" class="btn btn-sm btn-danger"
                                            onclick="return confirm('Xác nhận gỡ (xóa) bài này?');">
                                        <i class="bi bi-check-circle"></i> Gỡ bài
                                    </button>
                                </form>

                                <!-- Từ chối gỡ -->
                                <form action="${pageContext.request.contextPath}/admin/handle-remove"
                                      method="post" class="mt-1">
                                    <input type="hidden" name="id" value="${a.id}">
                                    <input type="hidden" name="action" value="reject">
                                    <input type="text" name="adminMessage"
                                           class="form-control form-control-sm mb-1"
                                           placeholder="Lý do từ chối (gửi cho user)">
                                    <button type="submit" class="btn btn-sm btn-secondary">
                                        <i class="bi bi-x-circle"></i> Giữ lại bài
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
