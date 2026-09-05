<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%
    request.setAttribute("pageTitle", "Danh sách đơn hàng");
    request.setAttribute("activeMenu", "order");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Đơn hàng | Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
    <style>
        .card { border: 0; border-radius: 8px; box-shadow: 0 4px 18px rgba(0,0,0,.06); }
        .card-header { background: transparent; border-bottom: 1px solid #eef1f5; padding: 18px 22px; }
        .table thead th { font-size: 12px; text-transform: uppercase; color: #64748b; }
        .btn-icon { display: inline-flex; align-items: center; gap: 6px; }
        .empty-state { padding: 40px 0; color: #94a3b8; }
    </style>
</head>
<body>
<div class="admin-layout">
    <%@ include file="fragments/sidebar.jsp" %>
    <div class="admin-main">
        <%@ include file="fragments/header.jsp" %>
        <main class="admin-content">
            <c:if test="${not empty param.message}"><div class="alert alert-success"><c:out value="${param.message}"/></div></c:if>
            <c:if test="${not empty param.error}"><div class="alert alert-danger"><c:out value="${param.error}"/></div></c:if>
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0 fw-semibold"><i class="fa-solid fa-receipt me-2 text-primary"></i>Đơn hàng</h5>
                </div>
                <div class="card-body">
                    <form method="get" class="row g-2 mb-3">
                        <div class="col-md-6"><input name="keyword" value="${param.keyword}" class="form-control" placeholder="Tìm theo mã đơn, khách hàng, số điện thoại..."></div>
                        <div class="col-md-4"><select name="status" class="form-select"><option value="">Tất cả trạng thái</option><c:forEach items="${statuses}" var="st"><option value="${st}" ${param.status == st ? 'selected' : ''}>${st}</option></c:forEach></select></div>
                        <div class="col-md-2"><button class="btn btn-outline-secondary w-100"><i class="fa-solid fa-magnifying-glass"></i></button></div>
                    </form>
                    <div class="table-responsive">
                        <table class="table table-hover align-middle">
                            <thead class="table-light"><tr><th>Mã</th><th>Khách hàng</th><th>Sản phẩm đặt</th><th>Ngày đặt</th><th>Tổng tiền</th><th>Trạng thái</th><th class="text-center">Thao tác</th></tr></thead>
                            <tbody>
                            <c:forEach items="${orders}" var="o">
                                <tr>
                                    <td>#${o.id}</td>
                                    <td><c:out value="${o.customerName}"/></td>
                                    <td>
                                        <c:forEach items="${o.items}" var="item">
                                            <div class="small"><strong><c:out value="${item.product.name}"/></strong> &times; ${item.quantity}</div>
                                        </c:forEach>
                                        <c:if test="${empty o.items}"><span class="text-muted small">Không có dữ liệu sản phẩm</span></c:if>
                                    </td>
                                    <td>${o.orderDate}</td>
                                    <td><fmt:formatNumber value="${o.totalAmount}" type="number"/> đ</td>
                                    <td><span class="badge text-bg-secondary">${o.status}</span></td>
                                    <td class="text-center"><a class="btn btn-sm btn-outline-primary btn-icon" href="${pageContext.request.contextPath}/admin/order/detail?id=${o.id}"><i class="fa-solid fa-eye"></i> Chi tiết</a></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty orders}"><tr><td colspan="7" class="text-center empty-state">Chưa có đơn hàng nào.</td></tr></c:if>
                            </tbody>
                        </table>
                    </div>
                    <c:if test="${totalPages > 1}">
                        <nav><ul class="pagination pagination-sm justify-content-end"><c:forEach begin="1" end="${totalPages}" var="i"><li class="page-item ${i == page ? 'active' : ''}"><a class="page-link" href="?page=${i}&keyword=${param.keyword}&status=${param.status}">${i}</a></li></c:forEach></ul></nav>
                    </c:if>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>
