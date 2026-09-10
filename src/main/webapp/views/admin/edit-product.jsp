<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sửa sản phẩm | Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
</head>
<body>
<div class="admin-layout"><%@ include file="fragments/sidebar.jsp" %><div class="admin-main"><%@ include file="fragments/header.jsp" %><main class="admin-content">
    <div class="mx-auto" style="max-width:900px"><div class="mb-3"><a href="${pageContext.request.contextPath}/admin/product/list" class="btn btn-outline-secondary btn-sm"><i class="fa-solid fa-arrow-left"></i> Quay lại</a></div>
    <c:if test="${empty product}"><div class="alert alert-danger">Không tìm thấy sản phẩm.</div></c:if>
    <c:if test="${not empty product}"><div class="card"><div class="card-header bg-white"><h5 class="mb-0">Sửa sản phẩm</h5></div><div class="card-body">
        <c:if test="${not empty error}"><div class="alert alert-danger"><c:out value="${error}"/></div></c:if>
        <form method="post" enctype="multipart/form-data"><input type="hidden" name="id" value="${product.id}">
            <div class="row g-3">
                <div class="col-md-8"><label class="form-label">Tên sản phẩm *</label><input name="name" value="${formSubmitted ? formName : product.name}" class="form-control ${not empty errors.name ? 'is-invalid' : ''}" maxlength="255" required><c:if test="${not empty errors.name}"><div class="invalid-feedback"><c:out value="${errors.name}"/></div></c:if></div>
                <div class="col-md-4"><label class="form-label">Danh mục *</label><select name="categoryId" class="form-select ${not empty errors.categoryId ? 'is-invalid' : ''}" required><c:forEach items="${categories}" var="c"><option value="${c.id}" ${(formSubmitted ? formCategoryId == c.id : product.category.id == c.id) ? 'selected' : ''}>${c.name}</option></c:forEach></select><c:if test="${not empty errors.categoryId}"><div class="invalid-feedback"><c:out value="${errors.categoryId}"/></div></c:if></div>
                <div class="col-md-4"><label class="form-label">Giá *</label><input type="number" min="0.01" step="0.01" name="price" value="${formSubmitted ? formPrice : product.price}" class="form-control ${not empty errors.price ? 'is-invalid' : ''}" required><c:if test="${not empty errors.price}"><div class="invalid-feedback"><c:out value="${errors.price}"/></div></c:if></div>
                <div class="col-md-4"><label class="form-label">Tồn kho *</label><input type="number" min="0" step="1" name="stockQuantity" value="${formSubmitted ? formStockQuantity : product.stockQuantity}" class="form-control ${not empty errors.stockQuantity ? 'is-invalid' : ''}" required><c:if test="${not empty errors.stockQuantity}"><div class="invalid-feedback"><c:out value="${errors.stockQuantity}"/></div></c:if></div>
                <div class="col-md-4"><label class="form-label">Trạng thái</label><select name="active" class="form-select ${not empty errors.active ? 'is-invalid' : ''}"><option value="true" ${(formSubmitted ? formActive == 'true' : product.active) ? 'selected' : ''}>Đang bán</option><option value="false" ${(formSubmitted ? formActive == 'false' : !product.active) ? 'selected' : ''}>Ngừng bán</option></select><c:if test="${not empty errors.active}"><div class="invalid-feedback"><c:out value="${errors.active}"/></div></c:if></div>
                <div class="col-12"><label class="form-label">Mô tả</label><textarea name="description" rows="4" class="form-control ${not empty errors.description ? 'is-invalid' : ''}">${formSubmitted ? formDescription : product.description}</textarea><c:if test="${not empty errors.description}"><div class="invalid-feedback"><c:out value="${errors.description}"/></div></c:if></div>
                <div class="col-md-6"><label class="form-label">Upload ảnh mới</label><input type="file" name="imageFile" class="form-control ${not empty errors.imageFile ? 'is-invalid' : ''}" accept="image/jpeg,image/png,image/gif,image/webp"><c:if test="${not empty errors.imageFile}"><div class="invalid-feedback"><c:out value="${errors.imageFile}"/></div></c:if></div>
                <div class="col-md-6"><label class="form-label">Hoặc nhập URL ảnh</label><input name="imageUrl" value="${formSubmitted ? formImageUrl : product.image}" class="form-control ${not empty errors.imageUrl ? 'is-invalid' : ''}" maxlength="500" placeholder="https://..."><c:if test="${not empty errors.imageUrl}"><div class="invalid-feedback"><c:out value="${errors.imageUrl}"/></div></c:if></div>
            </div>
            <div class="mt-4"><button class="btn btn-primary"><i class="fa-solid fa-floppy-disk"></i> Cập nhật</button> <a href="${pageContext.request.contextPath}/admin/product/list" class="btn btn-outline-secondary">Hủy</a></div>
        </form>
    </div></div></c:if></div>
</main></div></div>
</body></html>
