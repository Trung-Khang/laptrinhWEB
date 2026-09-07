<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="admin-header">
    <h1><c:out value="${requestScope.pageTitle}"/></h1>
    <div class="admin-account">
        Xin chào, <a href="${pageContext.request.contextPath}/profile" style="color: inherit; text-decoration: underline;"><strong><c:out value="${sessionScope.account.fullName}"/></strong></a>
        <a href="${pageContext.request.contextPath}/profile" style="margin: 0 8px; text-decoration: none; padding: 4px 10px; border-radius: 4px; border: 1px solid #0d6efd; color: #0d6efd; font-size: 13px;">Hồ sơ</a>
        <a href="${pageContext.request.contextPath}/logout" class="logout-button">Đăng xuất</a>
    </div>
</header>
