<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Chỉnh sửa bài viết</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
</head>
<body class="bg-light">

<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="bi bi-pencil-square"></i> Chỉnh sửa bài viết</h2>
        <div>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary me-2">Về trang chủ</a>
            <a href="${pageContext.request.contextPath}/my-articles" class="btn btn-outline-primary">
                <i class="bi bi-journal-text"></i> Bài viết của tôi
            </a>
        </div>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-body">

            <form action="${pageContext.request.contextPath}/update-article"
                  method="post"
                  enctype="multipart/form-data">

                <input type="hidden" name="id" value="${article.id}"/>

                <div class="mb-3">
                    <label class="form-label">Tiêu đề <span class="text-danger">*</span></label>
                    <input type="text" name="title" class="form-control" required
                           value="${article.title}">
                </div>

                <div class="mb-3">
                    <label class="form-label">Mô tả ngắn <span class="text-danger">*</span></label>
                    <textarea name="shortDescription" rows="3" class="form-control" required>${article.shortDescription}</textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label">Nội dung <span class="text-danger">*</span></label>
                    <textarea name="content" rows="10" class="form-control" required>${article.content}</textarea>
                </div>

                <div class="row">
                    <div class="mb-3 col-md-6">
                        <label class="form-label">Chuyên mục <span class="text-danger">*</span></label>
                        <select name="categoryId" class="form-select" required>
                            <c:forEach var="c" items="${categories}">
                                <option value="${c.id}"
                                        <c:if test="${c.id == article.categoryId}">selected</c:if>>
                                        ${c.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3 col-md-6">
                        <label class="form-label">Thumbnail</label><br>

                        <!-- Preview ảnh -->
                        <div class="mb-2">
                            <img id="thumbnailPreview"
                                 src="${not empty article.thumbnail ? pageContext.request.contextPath.concat('/images/').concat(article.thumbnail) : 'https://via.placeholder.com/300x200?text=Chưa+có+ảnh'}"
                                 alt="Preview"
                                 class="img-thumbnail"
                                 style="max-height: 200px; width: 100%; object-fit: contain;"
                                 onerror="this.src='https://via.placeholder.com/300x200?text=Lỗi+tải+ảnh'">
                        </div>

                        <input type="file"
                               name="thumbnail"
                               id="thumbnailInput"
                               accept="image/*"
                               class="form-control"
                               onchange="previewImage(this)">
                        <div class="form-text">Để trống nếu không muốn thay đổi ảnh.</div>
                    </div>
                </div>

                <div class="d-flex justify-content-between mt-4">
                    <div>
                        <button type="submit" name="action" value="save_draft" class="btn btn-warning">
                            <i class="bi bi-save"></i> Lưu thay đổi
                        </button>
                        <button type="submit" name="action" value="submit_pending" class="btn btn-success ms-2">
                            <i class="bi bi-send"></i> Gửi duyệt lại
                        </button>
                    </div>

                    <a href="${pageContext.request.contextPath}/my-articles" class="btn btn-outline-danger">
                        Hủy
                    </a>
                </div>

            </form>

        </div>
    </div>
</div>

<script>
    // Preview ảnh khi chọn file mới
    function previewImage(input) {
        const preview = document.getElementById('thumbnailPreview');

        if (input.files && input.files[0]) {
            const reader = new FileReader();

            reader.onload = function(e) {
                preview.src = e.target.result;
            };

            reader.readAsDataURL(input.files[0]);
        }
    }
</script>

</body>
</html>