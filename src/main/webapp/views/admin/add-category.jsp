<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<div class="d-flex justify-content-between align-items-center gap-3 mb-4">
    <div><p class="text-primary text-uppercase fw-semibold small mb-1">Bài tập 03</p><h2 class="h4 mb-0">Thêm danh mục</h2></div>
    <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-outline-secondary"><i class="fa-solid fa-arrow-left me-1"></i>Quay lại</a>
</div>
<section class="card shadow-sm mx-auto" style="max-width:760px;">
    <div class="card-header bg-white"><h3 class="h5 mb-0"><i class="fa-solid fa-folder-plus text-primary me-2"></i>Thông tin danh mục</h3></div>
    <div class="card-body p-4">
        <form action="${pageContext.request.contextPath}/admin/category/add" method="post" enctype="multipart/form-data">
            <div class="mb-3"><label for="cateName" class="form-label">Tên danh mục <span class="text-danger">*</span></label><input type="text" id="cateName" name="name" class="form-control" maxlength="150" required></div>
            <div class="mb-4"><label for="cateIcon" class="form-label">Icon danh mục</label><input type="file" id="cateIcon" name="icon" class="form-control" accept="image/png,image/jpeg,image/gif,image/webp"><div class="form-text">Có thể bỏ trống; hỗ trợ JPG, PNG, GIF hoặc WEBP.</div></div>
            <button type="submit" class="btn btn-primary"><i class="fa-solid fa-floppy-disk me-1"></i>Lưu danh mục</button>
        </form>
    </div>
</section>
