<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Đơn hàng | Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <style>
        :root{--sidebar-width:260px;--content-bg:#f4f6f9;--font-main:'Poppins',sans-serif}
        *{margin:0;padding:0;box-sizing:border-box} body{font-family:var(--font-main);background:var(--content-bg);color:#334155;font-size:14px}
        .sidebar{position:fixed;top:0;left:0;width:var(--sidebar-width);height:100vh;background:linear-gradient(180deg,#007bff 0%,#0056b3 100%);color:#fff;display:flex;flex-direction:column;box-shadow:2px 0 14px rgba(0,0,0,.14)}
        .sidebar-brand,.sidebar-user{display:flex;align-items:center;gap:12px;padding:22px 20px;border-bottom:1px solid rgba(255,255,255,.15)}
        .brand-icon,.avatar-circle{width:46px;height:46px;border-radius:10px;background:rgba(255,255,255,.2);display:flex;align-items:center;justify-content:center;font-size:20px}.avatar-circle{border-radius:50%;border:2px solid #fff}
        .brand-text{font-size:18px;font-weight:600;line-height:1.2}.brand-text small{display:block;font-size:11px;font-weight:400;opacity:.75}.user-name{font-weight:500}.user-role{font-size:12px;opacity:.75}
        .sidebar-menu{flex:1;padding:16px 12px}.menu-label{font-size:11px;text-transform:uppercase;letter-spacing:1px;opacity:.6;padding:8px 12px}.menu-item{display:flex;align-items:center;gap:12px;padding:12px 14px;margin-bottom:4px;border-radius:8px;color:rgba(255,255,255,.78);text-decoration:none;border-left:3px solid transparent}.menu-item i{width:20px;text-align:center}.menu-item:hover,.menu-item.active{background:rgba(255,255,255,.18);color:#fff}.menu-item.active{border-left-color:#fff;font-weight:500}.sidebar-footer{padding:16px 20px;border-top:1px solid rgba(255,255,255,.15);font-size:12px;opacity:.65}
        .main-wrapper{margin-left:var(--sidebar-width);min-height:100vh}.top-header{background:#fff;padding:14px 28px;display:flex;align-items:center;justify-content:space-between;box-shadow:0 2px 10px rgba(0,0,0,.06)}.page-title{font-size:18px;font-weight:600;margin:0}.main-content{padding:28px}.card{border:0;border-radius:12px;box-shadow:0 4px 18px rgba(0,0,0,.06)}.card-header{background:transparent;border-bottom:1px solid #eef1f5;padding:18px 22px}.table thead th{font-size:12px;text-transform:uppercase;color:#64748b}.btn-icon{display:inline-flex;align-items:center;gap:6px}.empty-state{padding:40px 0;color:#94a3b8}
        @media(max-width:767.98px){.sidebar{display:none}.main-wrapper{margin-left:0}.top-header{flex-direction:column;align-items:flex-start;gap:12px}}
    </style>
</head>
<body>
<aside class="sidebar">
    <div class="sidebar-brand"><span class="brand-icon"><i class="fa-solid fa-bag-shopping"></i></span><span class="brand-text">Admin Panel<small>Quản trị cửa hàng</small></span></div>
    <div class="sidebar-user"><div class="avatar-circle"><i class="fa-solid fa-user"></i></div><div><div class="user-name"><c:out value="${sessionScope.account.fullName}" default="Admin"/></div><div class="user-role">● Administrator</div></div></div>
    <nav class="sidebar-menu">
        <div class="menu-label">Menu chính</div>
        <a href="${pageContext.request.contextPath}/admin/category/list" class="menu-item"><i class="fa-solid fa-layer-group"></i> Quản lý Danh mục</a>
        <a href="${pageContext.request.contextPath}/admin/product/list" class="menu-item"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
        <a href="${pageContext.request.contextPath}/admin/order/list" class="menu-item active"><i class="fa-solid fa-cart-shopping"></i> Quản lý Đơn hàng</a>
        <a href="${pageContext.request.contextPath}/admin/user/list" class="menu-item"><i class="fa-solid fa-users"></i> Quản lý Người dùng</a>
        <div class="menu-label" style="margin-top:12px;">Khác</div><a href="${pageContext.request.contextPath}/admin/statistics" class="menu-item"><i class="fa-solid fa-chart-line"></i> Thống kê</a>
    </nav><div class="sidebar-footer">© 2024 Shopping Admin</div>
</aside>
<div class="main-wrapper">
    <header class="top-header"><h5 class="page-title">Danh Sách Đơn Hàng</h5><div><c:if test="${not empty sessionScope.account}">Xin chào, <strong><c:out value="${sessionScope.account.fullName}"/></strong></c:if><a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm ms-3 btn-icon"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a></div></header>
    <main class="main-content">
        <c:if test="${not empty param.message}"><div class="alert alert-success"><c:out value="${param.message}"/></div></c:if>
        <c:if test="${not empty param.error}"><div class="alert alert-danger"><c:out value="${param.error}"/></div></c:if>
        <div class="card"><div class="card-header"><h5 class="mb-0 fw-semibold"><i class="fa-solid fa-receipt me-2 text-primary"></i>Đơn hàng</h5></div><div class="card-body">
            <form method="get" class="row g-2 mb-3"><div class="col-md-6"><input name="keyword" value="${param.keyword}" class="form-control" placeholder="Tìm theo mã đơn, khách hàng, số điện thoại..."></div><div class="col-md-4"><select name="status" class="form-select"><option value="">Tất cả trạng thái</option><c:forEach items="${statuses}" var="st"><option value="${st}" ${param.status == st ? 'selected' : ''}>${st}</option></c:forEach></select></div><div class="col-md-2"><button class="btn btn-outline-secondary w-100"><i class="fa-solid fa-magnifying-glass"></i></button></div></form>
            <div class="table-responsive"><table class="table table-hover align-middle"><thead class="table-light"><tr><th>Mã</th><th>Khách hàng</th><th>SĐT</th><th>Ngày đặt</th><th>Tổng tiền</th><th>Trạng thái</th><th class="text-center">Thao tác</th></tr></thead><tbody>
            <c:forEach items="${orders}" var="o"><tr><td>#${o.id}</td><td><c:out value="${o.customerName}"/></td><td><c:out value="${o.phone}"/></td><td>${o.orderDate}</td><td><fmt:formatNumber value="${o.totalAmount}" type="number"/> đ</td><td><span class="badge text-bg-secondary">${o.status}</span></td><td class="text-center"><a class="btn btn-sm btn-outline-primary btn-icon" href="${pageContext.request.contextPath}/admin/order/detail?id=${o.id}"><i class="fa-solid fa-eye"></i> Chi tiết</a></td></tr></c:forEach>
            <c:if test="${empty orders}"><tr><td colspan="7" class="text-center empty-state">Chưa có đơn hàng nào.</td></tr></c:if>
            </tbody></table></div>
            <c:if test="${totalPages > 1}"><nav><ul class="pagination pagination-sm justify-content-end"><c:forEach begin="1" end="${totalPages}" var="i"><li class="page-item ${i == page ? 'active' : ''}"><a class="page-link" href="?page=${i}&keyword=${param.keyword}&status=${param.status}">${i}</a></li></c:forEach></ul></nav></c:if>
        </div></div>
    </main>
</div>
</body></html>

