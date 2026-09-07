<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Sản phẩm | Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root { --sidebar-width: 260px; --content-bg: #f4f6f9; --font-main: 'Poppins', sans-serif; }
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: var(--font-main); background: var(--content-bg); color: #334155; font-size: 14px; }
        .sidebar { position: fixed; top: 0; left: 0; width: var(--sidebar-width); height: 100vh; background: linear-gradient(180deg, #007bff 0%, #0056b3 100%); color: #fff; display: flex; flex-direction: column; z-index: 1040; box-shadow: 2px 0 14px rgba(0,0,0,.14); }
        .sidebar-brand, .sidebar-user { display: flex; align-items: center; gap: 12px; padding: 22px 20px; border-bottom: 1px solid rgba(255,255,255,.15); }
        .sidebar-brand { justify-content: center; padding: 24px 20px 20px; }
        .sidebar-logo { width: 190px; height: 70px; object-fit: contain; display: block; }
        .brand-icon, .avatar-circle { width: 46px; height: 46px; border-radius: 10px; background: rgba(255,255,255,.2); display: flex; align-items: center; justify-content: center; font-size: 20px; }
        .avatar-circle { border-radius: 50%; border: 2px solid #fff; }
        .brand-text { font-size: 18px; font-weight: 600; line-height: 1.2; }
        .brand-text small { display: block; font-size: 11px; font-weight: 400; opacity: .75; }
        .user-name { font-size: 14px; font-weight: 500; }
        .user-role { font-size: 12px; opacity: .75; }
        .sidebar-menu { flex: 1; padding: 16px 12px; overflow-y: auto; }
        .menu-label { font-size: 11px; text-transform: uppercase; letter-spacing: 1px; opacity: .6; padding: 8px 12px; margin-bottom: 4px; }
        .menu-item { display: flex; align-items: center; gap: 12px; padding: 12px 14px; margin-bottom: 4px; border-radius: 8px; color: rgba(255,255,255,.78); text-decoration: none; font-size: 14px; transition: .2s; border-left: 3px solid transparent; }
        .menu-item i { width: 20px; text-align: center; }
        .menu-item:hover, .menu-item.active { background: rgba(255,255,255,.18); color: #fff; }
        .menu-item.active { border-left-color: #fff; font-weight: 500; }
        .sidebar-footer { padding: 16px 20px; border-top: 1px solid rgba(255,255,255,.15); font-size: 12px; opacity: .65; }
        .main-wrapper { margin-left: var(--sidebar-width); min-height: 100vh; }
        .top-header { background: #fff; padding: 14px 28px; display: flex; align-items: center; justify-content: space-between; gap: 16px; box-shadow: 0 2px 10px rgba(0,0,0,.06); position: sticky; top: 0; z-index: 1020; }
        .page-title { font-size: 18px; font-weight: 600; color: #1e293b; margin: 0; }
        .main-content { padding: 28px; }
        .card { border: 0; border-radius: 12px; box-shadow: 0 4px 18px rgba(0,0,0,.06); }
        .card-header { background: transparent; border-bottom: 1px solid #eef1f5; padding: 18px 22px; }
        .table thead th { font-size: 12px; text-transform: uppercase; letter-spacing: .5px; color: #64748b; }
        .product-img { width: 64px; height: 64px; object-fit: cover; border-radius: 8px; background: #eef2f7; box-shadow: 0 2px 8px rgba(0,0,0,.12); }
        .btn-icon { display: inline-flex; align-items: center; gap: 6px; }
        .empty-state { padding: 40px 0; color: #94a3b8; }
        @media (max-width: 767.98px) { .sidebar { display:none; } .main-wrapper { margin-left:0; } .top-header { flex-direction:column; align-items:flex-start; } }
    </style>
</head>
<body>
<aside class="sidebar">
    <div class="sidebar-brand"><a href="${pageContext.request.contextPath}/admin/category/list" aria-label="KhangGear Admin"><img class="sidebar-logo" src="${pageContext.request.contextPath}/assets/images/khanggear-logo.png" alt="KhangGear"></a></div>
    <a href="${pageContext.request.contextPath}/profile" class="sidebar-user" style="text-decoration:none;color:inherit;"><div class="avatar-circle"><i class="fa-solid fa-user"></i></div><div><div class="user-name"><c:out value="${sessionScope.account.fullName}" default="Admin"/></div><div class="user-role">● ${sessionScope.account.roleid == 1 ? 'Administrator' : sessionScope.account.roleid == 2 ? 'Manager' : 'Customer'}</div></div></a>
    <nav class="sidebar-menu">
        <div class="menu-label">Menu chính</div>
        <a href="${pageContext.request.contextPath}/profile" class="menu-item"><i class="fa-solid fa-id-badge"></i> Hồ sơ cá nhân</a>
        <a href="${pageContext.request.contextPath}/admin/category/list" class="menu-item"><i class="fa-solid fa-layer-group"></i> Quản lý Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin/product/list" class="menu-item active"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
        <a href="${pageContext.request.contextPath}/admin/order/list" class="menu-item"><i class="fa-solid fa-cart-shopping"></i> Quản lý Đơn hàng</a>
        <a href="${pageContext.request.contextPath}/admin/user/list" class="menu-item"><i class="fa-solid fa-users"></i> Quản lý Người dùng</a>
        <div class="menu-label" style="margin-top:12px;">Khác</div>
        <a href="${pageContext.request.contextPath}/admin/statistics" class="menu-item"><i class="fa-solid fa-chart-line"></i> Thống kê</a>
    </nav>
    <div class="sidebar-footer">© 2024 Shopping Admin</div>
</aside>
<div class="main-wrapper">
    <header class="top-header">
        <h5 class="page-title">Danh Sách Sản Phẩm</h5>
        <div><c:if test="${not empty sessionScope.account}">Xin chào, <a href="${pageContext.request.contextPath}/profile" style="color:inherit;text-decoration:underline;"><strong><c:out value="${sessionScope.account.fullName}"/></strong></a></c:if> <a href="${pageContext.request.contextPath}/profile" class="btn btn-outline-primary btn-sm ms-2 btn-icon"><i class="fa-solid fa-user"></i> Hồ sơ</a> <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm ms-2 btn-icon"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a></div>
    </header>
    <main class="main-content">
        <c:if test="${not empty param.message}"><div class="alert alert-success"><c:out value="${param.message}"/></div></c:if>
        <c:if test="${not empty param.error}"><div class="alert alert-danger"><c:out value="${param.error}"/></div></c:if>
        <div class="card">
            <div class="card-header d-flex flex-wrap align-items-center justify-content-between gap-2">
                <h5 class="mb-0 fw-semibold"><i class="fa-solid fa-boxes-stacked me-2 text-primary"></i>Sản phẩm</h5>
                <a href="${pageContext.request.contextPath}/admin/product/add" class="btn btn-primary btn-sm btn-icon"><i class="fa-solid fa-plus"></i> Thêm sản phẩm</a>
            </div>
            <div class="card-body">
                <form method="get" class="row g-2 mb-3">
                    <div class="col-md-4"><input name="keyword" value="${param.keyword}" class="form-control" placeholder="Tìm sản phẩm..."></div>
                    <div class="col-md-3"><select name="categoryId" class="form-select"><option value="">Tất cả danh mục</option><c:forEach items="${categories}" var="c"><option value="${c.id}" ${param.categoryId == c.id ? 'selected' : ''}>${c.name}</option></c:forEach></select></div>
                    <div class="col-md-3"><select name="active" class="form-select"><option value="">Tất cả trạng thái</option><option value="true" ${param.active == 'true' ? 'selected' : ''}>Đang bán</option><option value="false" ${param.active == 'false' ? 'selected' : ''}>Ngừng bán</option></select></div>
                    <div class="col-md-2"><button class="btn btn-outline-secondary w-100"><i class="fa-solid fa-magnifying-glass"></i></button></div>
                </form>
                <div class="table-responsive">
                    <table class="table table-hover align-middle">
                        <thead class="table-light"><tr><th>STT</th><th>Ảnh</th><th>Tên sản phẩm</th><th>Danh mục</th><th>Giá</th><th>Tồn kho</th><th>Trạng thái</th><th class="text-center">Thao tác</th></tr></thead>
                        <tbody>
                        <c:forEach items="${products}" var="p" varStatus="s">
                            <tr>
                                <td>${(page - 1) * 6 + s.index + 1}</td>
                                <td><c:choose><c:when test="${not empty p.image && (fn:startsWith(p.image, 'http://') || fn:startsWith(p.image, 'https://'))}"><img src="${p.image}" class="product-img" alt="${p.name}"></c:when><c:when test="${not empty p.image}"><c:url value="/image" var="img"><c:param name="fname" value="${p.image}"/></c:url><img src="${img}" class="product-img" alt="${p.name}"></c:when><c:otherwise><img src="https://placehold.co/80x80?text=No+Image" class="product-img" alt="No image"></c:otherwise></c:choose></td>
                                <td class="fw-medium"><c:out value="${p.name}"/></td>
                                <td><c:out value="${p.category.name}"/></td>
                                <td><fmt:formatNumber value="${p.price}" type="number"/> đ</td>
                                <td>${p.stockQuantity}</td>
                                <td><span class="badge ${p.active ? 'text-bg-success' : 'text-bg-secondary'}">${p.active ? 'Đang bán' : 'Ngừng bán'}</span></td>
                                <td class="text-center">
                                    <a class="btn btn-sm btn-outline-info btn-icon" href="${pageContext.request.contextPath}/admin/product/detail?id=${p.id}"><i class="fa-solid fa-eye"></i> Xem</a>
                                    <a class="btn btn-sm btn-outline-primary btn-icon" href="${pageContext.request.contextPath}/admin/product/edit?id=${p.id}"><i class="fa-solid fa-pen"></i> Sửa</a>
                                    <form class="d-inline" method="post" action="${pageContext.request.contextPath}/admin/product/delete" onsubmit="return confirm('B&#7841;n c&#243; ch&#7855;c mu&#7889;n x&#243;a s&#7843;n ph&#7849;m n&#224;y?')">
                                        <input type="hidden" name="id" value="${p.id}">
                                        <button type="submit" class="btn btn-sm btn-outline-danger btn-icon"><i class="fa-solid fa-trash"></i> Xóa</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty products}"><tr><td colspan="8" class="text-center empty-state">Chưa có sản phẩm nào.</td></tr></c:if>
                        </tbody>
                    </table>
                </div>
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
                    <small class="text-secondary">Tổng số: ${totalItems} sản phẩm</small>
                    <c:if test="${totalPages > 1}"><nav aria-label="Phân trang sản phẩm"><ul class="pagination pagination-sm mb-0"><li class="page-item ${page <= 1 ? 'disabled' : ''}"><a class="page-link" href="?page=${page - 1}&keyword=${param.keyword}&categoryId=${param.categoryId}&active=${param.active}">Trước</a></li><c:forEach begin="1" end="${totalPages}" var="i"><li class="page-item ${i == page ? 'active' : ''}"><a class="page-link" href="?page=${i}&keyword=${param.keyword}&categoryId=${param.categoryId}&active=${param.active}">${i}</a></li></c:forEach><li class="page-item ${page >= totalPages ? 'disabled' : ''}"><a class="page-link" href="?page=${page + 1}&keyword=${param.keyword}&categoryId=${param.categoryId}&active=${param.active}">Sau</a></li></ul></nav></c:if>
                </div>
            </div>
        </div>
    </main>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
