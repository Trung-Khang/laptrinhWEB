<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Danh mục | Admin</title>

    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- FontAwesome 6 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <!-- Google Font: Poppins -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <style>
        :root {
            --sidebar-width: 260px;
            --content-bg: #f4f6f9;
            --font-main: 'Poppins', sans-serif;
        }

        * { margin: 0; padding: 0; box-sizing: border-box; }

        body {
            font-family: var(--font-main);
            background: var(--content-bg);
            color: #334155;
            font-size: 14px;
        }

        /* ===== SIDEBAR ===== */
        .sidebar {
            position: fixed;
            top: 0;
            left: 0;
            width: var(--sidebar-width);
            height: 100vh;
            background: linear-gradient(180deg, #007bff 0%, #0056b3 100%);
            color: #fff;
            display: flex;
            flex-direction: column;
            z-index: 1040;
            box-shadow: 2px 0 14px rgba(0, 0, 0, 0.14);
        }

        .sidebar-brand {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px 20px 20px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.15);
        }

        .sidebar-logo {
            width: 190px;
            height: 70px;
            object-fit: contain;
            display: block;
        }

        .sidebar-brand .brand-icon {
            width: 42px;
            height: 42px;
            border-radius: 10px;
            background: rgba(255, 255, 255, 0.2);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
        }

        .sidebar-brand .brand-text {
            font-size: 18px;
            font-weight: 600;
            line-height: 1.2;
        }

        .sidebar-brand .brand-text small {
            display: block;
            font-size: 11px;
            font-weight: 400;
            opacity: 0.75;
        }

        .sidebar-user {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 18px 20px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.15);
        }

        .avatar-circle {
            width: 46px;
            height: 46px;
            border-radius: 50%;
            border: 2px solid #fff;
            background: rgba(255, 255, 255, 0.22);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 19px;
            color: #fff;
            flex-shrink: 0;
            overflow: hidden;
        }

        .avatar-circle img { width: 100%; height: 100%; object-fit: cover; }

        .sidebar-user .user-name {
            font-size: 14px;
            font-weight: 500;
            line-height: 1.3;
        }

        .sidebar-user .user-role {
            font-size: 12px;
            opacity: 0.75;
        }

        .sidebar-menu {
            flex: 1;
            padding: 16px 12px;
            overflow-y: auto;
        }

        .sidebar-menu .menu-label {
            font-size: 11px;
            text-transform: uppercase;
            letter-spacing: 1px;
            opacity: 0.6;
            padding: 8px 12px;
            margin-bottom: 4px;
        }

        .sidebar-menu a.menu-item {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 12px 14px;
            margin-bottom: 4px;
            border-radius: 8px;
            color: rgba(255, 255, 255, 0.78);
            text-decoration: none;
            font-size: 14px;
            font-weight: 400;
            transition: all 0.2s ease;
            border-left: 3px solid transparent;
        }

        .sidebar-menu a.menu-item i {
            width: 20px;
            text-align: center;
            font-size: 15px;
        }

        .sidebar-menu a.menu-item:hover {
            background: rgba(255, 255, 255, 0.12);
            color: #fff;
        }

        .sidebar-menu a.menu-item.active {
            background: rgba(255, 255, 255, 0.18);
            color: #fff;
            border-left: 3px solid #fff;
            font-weight: 500;
        }

        .sidebar-footer {
            padding: 16px 20px;
            border-top: 1px solid rgba(255, 255, 255, 0.15);
            font-size: 12px;
            opacity: 0.65;
        }

        /* ===== MAIN ===== */
        .main-wrapper {
            margin-left: var(--sidebar-width);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }

        .top-header {
            background: #fff;
            padding: 14px 28px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 16px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
            position: sticky;
            top: 0;
            z-index: 1020;
        }

        .top-header .page-title {
            font-size: 18px;
            font-weight: 600;
            color: #1e293b;
            margin: 0;
        }

        .header-right {
            display: flex;
            align-items: center;
            gap: 16px;
        }

        .header-greeting {
            font-size: 14px;
            color: #475569;
        }

        .header-greeting strong { color: #1e293b; }

        .main-content {
            flex: 1;
            padding: 28px;
        }

        /* ===== CARD ===== */
        .card {
            border: 0;
            border-radius: 12px;
            box-shadow: 0 4px 18px rgba(0, 0, 0, 0.06);
        }

        .card .card-header {
            background: transparent;
            border-bottom: 1px solid #eef1f5;
            padding: 18px 22px;
        }

        /* ===== TABLE ===== */
        .table thead th {
            font-size: 12px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            color: #64748b;
            border-bottom-width: 1px;
        }

        .cate-icon {
            width: 56px;
            height: 56px;
            object-fit: cover;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.12);
        }

        .btn-icon {
            display: inline-flex;
            align-items: center;
            gap: 6px;
        }

        .empty-state {
            padding: 40px 0;
            color: #94a3b8;
            font-size: 14px;
        }

        /* ===== RESPONSIVE ===== */
        @media (max-width: 991.98px) {
            .sidebar { width: 220px; }
            .main-wrapper { margin-left: 220px; }
        }

        @media (max-width: 767.98px) {
            .sidebar { display: none; }
            .main-wrapper { margin-left: 0; }
            .top-header { flex-direction: column; align-items: flex-start; }
        }
    </style>
</head>
<body>

    <!-- ===== SIDEBAR ===== -->
    <aside class="sidebar">
        <div class="sidebar-brand">
            <a href="${pageContext.request.contextPath}/admin/category/list" aria-label="KhangGear Admin">
                <img class="sidebar-logo" src="${pageContext.request.contextPath}/assets/images/khanggear-logo.png" alt="KhangGear">
            </a>
        </div>

        <div class="sidebar-user">
            <div class="avatar-circle">
                <i class="fa-solid fa-user"></i>
            </div>
            <div>
                <div class="user-name">
                    <c:choose>
                        <c:when test="${not empty sessionScope.account}"><c:out value="${sessionScope.account.fullName}"/></c:when>
                        <c:otherwise>Admin</c:otherwise>
                    </c:choose>
                </div>
                <div class="user-role"><i class="fa-solid fa-circle" style="font-size:6px; vertical-align:middle;"></i>&nbsp;${sessionScope.account.roleid == 1 ? 'Administrator' : sessionScope.account.roleid == 2 ? 'Manager' : 'Customer'}</div>
            </div>
        </div>

        <nav class="sidebar-menu">
            <div class="menu-label">Menu chính</div>
            <a href="${pageContext.request.contextPath}/admin/category/list" class="menu-item active">
                <i class="fa-solid fa-layer-group"></i> Quản lý Danh mục
            </a>
            <a href="${pageContext.request.contextPath}/admin/product/list" class="menu-item"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
            <a href="${pageContext.request.contextPath}/admin/order/list" class="menu-item"><i class="fa-solid fa-cart-shopping"></i> Quản lý Đơn hàng</a>
            <a href="${pageContext.request.contextPath}/admin/user/list" class="menu-item"><i class="fa-solid fa-users"></i> Quản lý Người dùng</a>
            <div class="menu-label" style="margin-top:12px;">Khác</div>
            <a href="${pageContext.request.contextPath}/admin/statistics" class="menu-item"><i class="fa-solid fa-chart-line"></i> Thống kê</a>
        </nav>

        <div class="sidebar-footer">
            <i class="fa-regular fa-copyright"></i> 2024 Shopping Admin
        </div>
    </aside>

    <!-- ===== MAIN ===== -->
    <div class="main-wrapper">
        <header class="top-header">
            <h5 class="page-title">Danh Sách Danh Mục</h5>
            <div class="header-right">
                <c:if test="${not empty sessionScope.account}">
                    <span class="header-greeting">Xin chào, <strong><c:out value="${sessionScope.account.fullName}"/></strong></span>
                </c:if>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm btn-icon">
                    <i class="fa-solid fa-right-from-bracket"></i> Đăng xuất
                </a>
            </div>
        </header>

        <main class="main-content">
            <c:if test="${not empty param.message}"><div class="alert alert-success"><c:out value="${param.message}"/></div></c:if>
            <c:if test="${not empty param.error}"><div class="alert alert-danger"><c:out value="${param.error}"/></div></c:if>
                <div class="card-header d-flex flex-wrap align-items-center justify-content-between gap-2">
                    <h5 class="mb-0 fw-semibold"><i class="fa-solid fa-table-list me-2 text-primary"></i>Danh mục sản phẩm</h5>
                    <div class="d-flex flex-wrap align-items-center gap-2">
                        <!-- Form tìm kiếm -->
                        <form action="${pageContext.request.contextPath}/admin/category/list" method="get" class="d-flex" role="search">
                            <div class="input-group input-group-sm">
                                <input type="text" name="keyword" value="${param.keyword}" class="form-control" placeholder="Tìm danh mục..." />
                                <button class="btn btn-outline-secondary" type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
                            </div>
                        </form>
                        <a href="${pageContext.request.contextPath}/admin/category/add" class="btn btn-primary btn-sm btn-icon">
                            <i class="fa-solid fa-plus"></i> Thêm danh mục mới
                        </a>
                    </div>
                </div>

                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover align-middle mb-0">
                            <thead class="table-light">
                                <tr>
                                    <th scope="col" style="width:70px;">STT</th>
                                    <th scope="col">Tên danh mục</th>
                                    <th scope="col" style="width:120px;">Icon</th>
                                    <th scope="col" style="width:180px; text-align:center;">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${cateList}" var="cate" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td class="fw-medium">${cate.name}</td>
                                        <td>
                                            <c:url value="/image" var="imageUrl">
                                                <c:param name="fname" value="${cate.icon}" />
                                            </c:url>
                                            <img src="${imageUrl}" class="cate-icon" alt="${cate.name}" />
                                        </td>
                                        <td class="text-center">
                                            <a href="${pageContext.request.contextPath}/admin/category/edit?id=${cate.id}"
                                               class="btn btn-sm btn-outline-primary btn-icon me-1">
                                                <i class="fa-solid fa-pen"></i> Sửa
                                            </a>
                                            <form method="post" action="${pageContext.request.contextPath}/admin/category/delete" class="d-inline" onsubmit="return confirm('Bạn có chắc muốn xóa danh mục này?');">
                                                <input type="hidden" name="id" value="${cate.id}">
                                            <button type="submit" class="btn btn-sm btn-outline-danger btn-icon">
                                                <i class="fa-solid fa-trash"></i> Xóa
                                            </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty cateList}">
                                    <tr>
                                        <td colspan="4" class="text-center empty-state">
                                            <i class="fa-regular fa-folder-open mb-2 d-block" style="font-size:34px;"></i>
                                            Không có danh mục nào.
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </main>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
