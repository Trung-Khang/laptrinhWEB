<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${empty requestScope.pageTitle ? 'Bài tập 03 | KhangGear' : requestScope.pageTitle}" /></title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
    <sitemesh:write property="head"/>
    <style>
        body { background: #f4f6f9; }
        .exercise03-navbar { background: #0d6efd; }
        .exercise03-navbar .navbar-brand, .exercise03-navbar .nav-link { color: #fff; }
        .exercise03-navbar .nav-link:hover { color: #dbeafe; }
        .exercise03-shell { min-height: calc(100vh - 122px); }
        .exercise03-content { padding: 1.5rem 0 2.5rem; }
        .exercise03-footer { background: #0f172a; color: #cbd5e1; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg exercise03-navbar shadow-sm">
        <div class="container-fluid px-4">
            <a class="navbar-brand fw-semibold" href="${pageContext.request.contextPath}/admin/category/list">
                <img src="${pageContext.request.contextPath}/assets/images/khanggear-logo.png" alt="KhangGear" height="42">
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#exercise03Menu" aria-controls="exercise03Menu" aria-expanded="false" aria-label="Mở menu">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="exercise03Menu">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/category/list">Danh mục</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/product/list">Sản phẩm</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/order/list">Đơn hàng</a></li>
                </ul>
                <div class="d-flex align-items-center gap-3 text-white">
                    <span>Xin chào, <strong><c:out value="${sessionScope.account.fullName}" /></strong></span>
                    <a class="btn btn-light btn-sm" href="${pageContext.request.contextPath}/logout">
                        <i class="fa-solid fa-right-from-bracket me-1"></i>Đăng xuất
                    </a>
                </div>
            </div>
        </div>
    </nav>

    <main class="container-fluid exercise03-shell exercise03-content">
        <sitemesh:write property="body"/>
    </main>

    <footer class="exercise03-footer py-3">
        <div class="container-fluid px-4 small">KhangGear · Bài tập 03 · Quản trị cửa hàng</div>
    </footer>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
