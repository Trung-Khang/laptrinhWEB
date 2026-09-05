<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%
    request.setAttribute("pageTitle", "Chi tiết sản phẩm");
    request.setAttribute("activeMenu", "product");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Sản phẩm | Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
    <style>
        .product-detail-wrap { max-width: 960px; margin: 0 auto; }
        .product-image { width: 100%; max-width: 260px; aspect-ratio: 1; object-fit: cover; border-radius: 8px; background: #eef2f7; box-shadow: 0 4px 18px rgba(0,0,0,.08); }
    </style>
</head>
<body>
<div class="admin-layout">
    <%@ include file="fragments/sidebar.jsp" %>
    <div class="admin-main">
        <%@ include file="fragments/header.jsp" %>
        <main class="admin-content">
            <div class="product-detail-wrap">
                <div class="mb-3">
                    <a href="${pageContext.request.contextPath}/admin/product/list" class="btn btn-outline-secondary btn-sm">Quay lại</a>
                </div>

                <c:if test="${empty product}">
                    <div class="alert alert-danger">Không tìm thấy sản phẩm.</div>
                </c:if>

                <c:if test="${not empty product}">
                    <div class="card border-0 shadow-sm">
                        <div class="card-body p-4">
                            <div class="row g-4">
                                <div class="col-md-4">
                                    <c:choose>
                                        <c:when test="${not empty product.image && (fn:startsWith(product.image, 'http://') || fn:startsWith(product.image, 'https://'))}">
                                            <img class="product-image" src="${product.image}" alt="${product.name}">
                                        </c:when>
                                        <c:when test="${not empty product.image}">
                                            <c:url value="/image" var="img">
                                                <c:param name="fname" value="${product.image}"/>
                                            </c:url>
                                            <img class="product-image" src="${img}" alt="${product.name}">
                                        </c:when>
                                        <c:otherwise>
                                            <img class="product-image" src="https://placehold.co/260x260?text=No+Image" alt="No image">
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="col-md-8">
                                    <h3 class="mb-2"><c:out value="${product.name}"/></h3>
                                    <p class="text-muted mb-3"><c:out value="${product.category.name}"/></p>
                                    <p><strong>Giá:</strong> <fmt:formatNumber value="${product.price}" type="number"/> đ</p>
                                    <p><strong>Tồn kho:</strong> ${product.stockQuantity}</p>
                                    <p><strong>Trạng thái:</strong> ${product.active ? 'Đang bán' : 'Ngừng bán'}</p>
                                    <p class="mb-1"><strong>Mô tả:</strong></p>
                                    <p><c:out value="${product.description}" default="Chưa có mô tả"/></p>
                                    <a href="${pageContext.request.contextPath}/admin/product/edit?id=${product.id}" class="btn btn-primary">Sửa sản phẩm</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </main>
    </div>
</div>
</body>
</html>
