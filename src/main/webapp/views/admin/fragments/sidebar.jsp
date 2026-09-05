<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<aside class="admin-sidebar">
    <a href="${pageContext.request.contextPath}/admin/category/list" aria-label="KhangGear Admin">
        <img class="admin-logo" src="${pageContext.request.contextPath}/assets/images/khanggear-logo.png" alt="KhangGear">
    </a>
    <nav class="admin-nav" aria-label="Điều hướng quản trị">
        <a class="${requestScope.activeMenu == 'category' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/category/list">Danh mục</a>
        <a class="${requestScope.activeMenu == 'product' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/product/list">Sản phẩm</a>
        <a class="${requestScope.activeMenu == 'order' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/order/list">Đơn hàng</a>
        <a class="${requestScope.activeMenu == 'user' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/user/list">Người dùng</a>
        <a class="${requestScope.activeMenu == 'statistics' ? 'active' : ''}" href="${pageContext.request.contextPath}/admin/statistics">Thống kê</a>
    </nav>
</aside>
