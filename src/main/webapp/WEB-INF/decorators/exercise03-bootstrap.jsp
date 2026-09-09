<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin-layout.css">
    <sitemesh:write property="head"/>
</head>
<body>
<div class="admin-layout">
    <%@ include file="../../views/admin/fragments/sidebar.jsp" %>
    <div class="admin-main">
        <%@ include file="../../views/admin/fragments/header.jsp" %>
        <main class="admin-content">
            <sitemesh:write property="body"/>
        </main>
        <footer class="border-top bg-white px-4 py-3 text-secondary small">KhangGear Admin</footer>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
