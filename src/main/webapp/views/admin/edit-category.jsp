<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sửa Danh mục | Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root { --sidebar-width: 260px; --content-bg: #f4f6f9; --font-main: 'Poppins', sans-serif; }
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: var(--font-main); background: var(--content-bg); color: #334155; font-size: 14px; }
        .sidebar { position: fixed; top: 0; left: 0; width: var(--sidebar-width); height: 100vh; background: linear-gradient(180deg, #007bff 0%, #0056b3 100%); color: #fff; display: flex; flex-direction: column; z-index: 1040; box-shadow: 2px 0 14px rgba(0, 0, 0, 0.14); }
        .sidebar-brand { display: flex; align-items: center; gap: 12px; padding: 22px 20px; border-bottom: 1px solid rgba(255, 255, 255, 0.15); }
        .sidebar-brand .brand-icon { width: 42px; height: 42px; border-radius: 10px; background: rgba(255, 255, 255, 0.2); display: flex; align-items: center; justify-content: center; font-size: 20px; }
        .sidebar-brand .brand-text { font-size: 18px; font-weight: 600; line-height: 1.2; }
        .sidebar-brand .brand-text small { display: block; font-size: 11px; font-weight: 400; opacity: 0.75; }
        .sidebar-user { display: flex; align-items: center; gap: 12px; padding: 18px 20px; border-bottom: 1px solid rgba(255, 255, 255, 0.15); }
        .avatar-circle { width: 46px; height: 46px; border-radius: 50%; border: 2px solid #fff; background: rgba(255, 255, 255, 0.22); display: flex; align-items: center; justify-content: center; font-size: 19px; color: #fff; flex-shrink: 0; overflow: hidden; }
        .avatar-circle img { width: 100%; height: 100%; object-fit: cover; }
        .sidebar-user .user-name { font-size: 14px; font-weight: 500; line-height: 1.3; }
        .sidebar-user .user-role { font-size: 12px; opacity: 0.75; }
        .sidebar-menu { flex: 1; padding: 16px 12px; overflow-y: auto; }
        .sidebar-menu .menu-label { font-size: 11px; text-transform: uppercase; letter-spacing: 1px; opacity: 0.6; padding: 8px 12px; margin-bottom: 4px; }
        .sidebar-menu a.menu-item { display: flex; align-items: center; gap: 12px; padding: 12px 14px; margin-bottom: 4px; border-radius: 8px; color: rgba(255, 255, 255, 0.78); text-decoration: none; font-size: 14px; font-weight: 400; transition: all 0.2s ease; border-left: 3px solid transparent; }
        .sidebar-menu a.menu-item i { width: 20px; text-align: center; font-size: 15px; }
        .sidebar-menu a.menu-item:hover { background: rgba(255, 255, 255, 0.12); color: #fff; }
        .sidebar-menu a.menu-item.active { background: rgba(255, 255, 255, 0.18); color: #fff; border-left: 3px solid #fff; font-weight: 500; }
        .sidebar-footer { padding: 16px 20px; border-top: 1px solid rgba(255, 255, 255, 0.15); font-size: 12px; opacity: 0.65; }
        .main-wrapper { margin-left: var(--sidebar-width); min-height: 100vh; display: flex; flex-direction: column; }
        .top-header { background: #fff; padding: 14px 28px; display: flex; align-items: center; justify-content: space-between; gap: 16px; box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06); position: sticky; top: 0; z-index: 1020; }
        .top-header .page-title { font-size: 18px; font-weight: 600; color: #1e293b; margin: 0; }
        .header-right { display: flex; align-items: center; gap: 16px; }
        .header-greeting { font-size: 14px; color: #475569; }
        .header-greeting strong { color: #1e293b; }
        .main-content { flex: 1; padding: 28px; }
        .card { border: 0; border-radius: 12px; box-shadow: 0 4px 18px rgba(0, 0, 0, 0.06); }
        .card .card-header { background: transparent; border-bottom: 1px solid #eef1f5; padding: 18px 22px; }
        .form-label { font-weight: 600; color: #1e293b; }
        .form-control { border-radius: 8px; padding: 10px 14px; border-color: #e2e8f0; }
        .form-control:focus { border-color: #007bff; box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.15); }
        .btn-icon { display: inline-flex; align-items: center; gap: 6px; }
        .icon-preview { width: 130px; height: 130px; border: 1px dashed #cbd5e1; border-radius: 12px; padding: 8px; background: #f8fafc; box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08); display: inline-block; }
        .icon-preview img { width: 100%; height: 100%; object-fit: cover; border-radius: 8px; }
        @media (max-width: 991.98px) { .sidebar { width: 220px; } .main-wrapper { margin-left: 220px; } }
        @media (max-width: 767.98px) { .sidebar { display: none; } .main-wrapper { margin-left: 0; } .top-header { flex-direction: column; align-items: flex-start; } }
    </style>
</head>
<body>
    <aside class="sidebar">
        <div class="sidebar-brand">
            <span class="brand-icon"><i class="fa-solid fa-bag-shopping"></i></span>
            <span class="brand-text">Admin Panel<small>Quản trị cửa hàng</small></span>
        </div>
        <div class="sidebar-user">
            <div class="avatar-circle"><i class="fa-solid fa-user"></i></div>
            <div>
                <div class="user-name">
                    <c:choose>
                        <c:when test="${not empty sessionScope.account}">${sessionScope.account.fullName}</c:when>
                        <c:otherwise>Admin</c:otherwise>
                    </c:choose>
                </div>
                <div class="user-role"><i class="fa-solid fa-circle" style="font-size:6px; vertical-align:middle;"></i>&nbsp;Administrator</div>
            </div>
        </div>
        <nav class="sidebar-menu">
            <div class="menu-label">Menu chính</div>
            <a href="${pageContext.request.contextPath}/admin/category/list" class="menu-item active"><i class="fa-solid fa-layer-group"></i> Quản lý Danh mục</a>
            <a href="${pageContext.request.contextPath}/admin/product/list" class="menu-item"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
            <a href="${pageContext.request.contextPath}/admin/order/list" class="menu-item"><i class="fa-solid fa-cart-shopping"></i> Quản lý Đơn hàng</a>
            <a href="${pageContext.request.contextPath}/admin/user/list" class="menu-item"><i class="fa-solid fa-users"></i> Quản lý Người dùng</a>
            <div class="menu-label" style="margin-top:12px;">Khác</div>
            <a href="${pageContext.request.contextPath}/admin/statistics" class="menu-item"><i class="fa-solid fa-chart-line"></i> Thống kê</a>
        </nav>
        <div class="sidebar-footer"><i class="fa-regular fa-copyright"></i> 2024 Shopping Admin</div>
    </aside>
    <div class="main-wrapper">
        <header class="top-header">
            <h5 class="page-title">Sửa Danh Mục</h5>
            <div class="header-right">
                <c:if test="${not empty sessionScope.account}">
                    <span class="header-greeting">Xin chào, <strong>${sessionScope.account.fullName}</strong></span>
                </c:if>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm btn-icon"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a>
            </div>
        </header>
        <main class="main-content">
            <c:if test="${empty category}">
                <div class="card mx-auto" style="max-width:800px;">
                    <div class="card-body text-center p-5">
                        <i class="fa-regular fa-circle-xmark d-block mb-3" style="font-size:44px; color:#dc3545;"></i>
                        <h5 class="fw-semibold">Không tìm thấy danh mục!</h5>
                        <p class="text-muted mb-4">Danh mục có thể đã bị xóa hoặc ID không hợp lệ.</p>
                        <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-primary btn-icon"><i class="fa-solid fa-arrow-left"></i> Quay lại danh sách</a>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty category}">
                <div class="card mx-auto" style="max-width:800px;">
                    <div class="card-header">
                        <h5 class="mb-0 fw-semibold"><i class="fa-solid fa-pen me-2 text-primary"></i>Chỉnh sửa danh mục</h5>
                    </div>
                    <div class="card-body p-4">
                        <form action="${pageContext.request.contextPath}/admin/category/edit" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="id" value="${category.id}" />
                            <div class="mb-3">
                                <label for="cateName" class="form-label">Tên danh mục <span class="text-danger">*</span></label>
                                <input type="text" id="cateName" name="name" value="${category.name}" class="form-control" required />
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Icon hiện tại</label><br/>
                                <span class="icon-preview">
                                    <c:url value="/image" var="imageUrl">
                                        <c:param name="fname" value="${category.icon}" />
                                    </c:url>
                                    <img src="${imageUrl}" alt="${category.name}" />
                                </span>
                            </div>
                            <div class="mb-4">
                                <label for="cateIcon" class="form-label">Chọn icon mới (nếu muốn đổi)</label>
                                <input type="file" id="cateIcon" name="icon" class="form-control" accept="image/*" />
                                <div class="form-text">Để trống nếu muốn giữ nguyên icon hiện tại.</div>
                            </div>
                            <div class="d-flex gap-2">
                                <button type="submit" class="btn btn-primary btn-icon"><i class="fa-solid fa-floppy-disk"></i> Cập nhật</button>
                                <a href="${pageContext.request.contextPath}/admin/category/list" class="btn btn-outline-secondary btn-icon"><i class="fa-solid fa-arrow-left"></i> Hủy</a>
                            </div>
                        </form>
                    </div>
                </div>
            </c:if>
        </main>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
