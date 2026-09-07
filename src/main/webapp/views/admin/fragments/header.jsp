<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="admin-header">
    <h1><c:out value="${requestScope.pageTitle}"/></h1>
    <div class="admin-account">
        Xin chào, <strong><c:out value="${sessionScope.account.fullName}"/></strong>
        <a href="${pageContext.request.contextPath}/logout" class="logout-button">Đăng xuất</a>
    </div>
</header>
