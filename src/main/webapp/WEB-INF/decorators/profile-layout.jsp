<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><% out.write("<sitemesh:write property=\"title\"/>"); %></title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="<%=contextPath%>/assets/css/profile-layout.css">
    <% out.write("<sitemesh:write property=\"head\"/>"); %>
</head>
<body class="profile-shell">
    <header class="profile-header">
        <a class="profile-brand" href="<%=contextPath%>/home" aria-label="KhangGear">
            <img src="<%=contextPath%>/assets/images/khanggear-logo.png" alt="KhangGear">
        </a>
        <nav class="profile-nav" aria-label="Menu người dùng">
            <a href="<%=contextPath%>/home">Trang chủ</a>
            <a href="<%=contextPath%>/product">Sản phẩm</a>
            <a class="active" href="<%=contextPath%>/profile">Hồ sơ</a>
            <a href="<%=contextPath%>/account/orders">Đơn hàng</a>
        </nav>
        <div class="profile-account">
            <span class="user-greeting"><c:out value="${sessionScope.account.fullName}" default="${sessionScope.account.userName}"/></span>
            <a class="logout-link" href="<%=contextPath%>/logout"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a>
        </div>
    </header>
    <main class="profile-main">
        <% out.write("<sitemesh:write property=\"body\"/>"); %>
    </main>
</body>
</html>
