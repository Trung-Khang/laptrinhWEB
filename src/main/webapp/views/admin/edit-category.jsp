<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="d-flex justify-content-between align-items-center gap-3 mb-4">
    <div><p class="text-primary text-uppercase fw-semibold small mb-1">Bài tập 03</p><h2 class="h4 mb-0">Sửa danh mục</h2></div>
    <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-outline-secondary"><i class="fa-solid fa-arrow-left me-1"></i>Quay lại</a>
</div>
<section class="card shadow-sm mx-auto" style="max-width:760px;">
    <div class="card-header bg-white"><h3 class="h5 mb-0"><i class="fa-solid fa-pen text-primary me-2"></i>Cập nhật thông tin</h3></div>
    <div class="card-body p-4">
        <c:choose>
            <c:when test="${empty category}"><div class="alert alert-warning mb-0">Không tìm thấy danh mục cần sửa.</div></c:when>
            <c:otherwise>
                <form action="${pageContext.request.contextPath}/admin/category/edit" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="id" value="${category.id}">
                    <div class="mb-3"><label for="cateName" class="form-label">Tên danh mục <span class="text-danger">*</span></label><input type="text" id="cateName" name="name" value="${category.name}" class="form-control" maxlength="150" required></div>
                    <c:if test="${not empty category.icon}"><div class="mb-3"><label class="form-label d-block">Icon hiện tại</label><img src="${pageContext.request.contextPath}/image?fname=${category.icon}" alt="${category.name}" class="rounded border" style="width:120px;height:120px;object-fit:cover;"></div></c:if>
                    <div class="mb-4"><label for="cateIcon" class="form-label">Chọn icon mới</label><input type="file" id="cateIcon" name="icon" class="form-control" accept="image/png,image/jpeg,image/gif,image/webp"><div class="form-text">Để trống nếu muốn giữ icon hiện tại.</div></div>
                    <button type="submit" class="btn btn-primary"><i class="fa-solid fa-floppy-disk me-1"></i>Cập nhật</button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</section>
