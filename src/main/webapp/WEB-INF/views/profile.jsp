<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%
    String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ cá nhân | KhangGear</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="<%=contextPath%>/assets/css/profile-layout.css">
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
            <c:if test="${sessionScope.account.roleid == 1 || sessionScope.account.roleid == 2}">
                <a href="<%=contextPath%>/admin/category/list">Trang Quản trị</a>
            </c:if>
            <a href="<%=contextPath%>/account/orders">Đơn hàng</a>
        </nav>
        <div class="profile-account">
            <span>Xin chào, <strong><c:out value="${sessionScope.account.fullName}" default="${sessionScope.account.userName}"/></strong></span>
            <a class="logout-link" href="<%=contextPath%>/logout"><i class="fa-solid fa-right-from-bracket"></i> Đăng xuất</a>
        </div>
    </header>
    <main class="profile-main">
    <section class="profile-card">
        <div class="profile-heading">
            <div>
                <p class="eyebrow">KhangGear Account</p>
                <h1>Hồ sơ cá nhân</h1>
                <p>Cập nhật họ tên và số điện thoại dùng cho tài khoản của bạn.</p>
            </div>
            <div class="avatar-preview">
                <c:choose>
                    <c:when test="${not empty profileUser.avatar && (fn:startsWith(profileUser.avatar, 'http://') || fn:startsWith(profileUser.avatar, 'https://'))}">
                        <img src="${profileUser.avatar}" alt="Ảnh đại diện">
                    </c:when>
                    <c:when test="${not empty profileUser.avatar}">
                        <c:url value="/image" var="avatarUrl">
                            <c:param name="fname" value="${profileUser.avatar}"/>
                        </c:url>
                        <img src="${avatarUrl}" alt="Ảnh đại diện">
                    </c:when>
                    <c:otherwise>
                        <i class="fa-solid fa-user"></i>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <c:if test="${not empty flashSuccess}">
            <div class="alert success"><c:out value="${flashSuccess}"/></div>
        </c:if>
        <c:if test="${not empty flashError}">
            <div class="alert error"><c:out value="${flashError}"/></div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/profile" enctype="multipart/form-data" class="profile-form">
            <label>
                Tên đăng nhập
                <input value="${profileUser.userName}" disabled>
            </label>
            <label>
                Email
                <input value="${profileUser.email}" disabled>
            </label>
            <label>
                Họ và tên
                <input name="fullname" required maxlength="150" value="${profileUser.fullName}">
            </label>
            <label>
                Số điện thoại
                <input name="phone" maxlength="30" value="${profileUser.phone}">
            </label>
            <label>
                Ảnh đại diện mới
                <input type="file" name="avatar" accept="image/jpeg,image/png,image/webp">
            </label>
            <p class="avatar-note">Hỗ trợ định dạng JPG, PNG, WEBP. Dung lượng tối đa 2MB. Để trống nếu muốn giữ ảnh đại diện hiện tại.</p>
            <div class="form-actions">
                <button type="submit"><i class="fa-solid fa-floppy-disk"></i> Lưu thay đổi</button>
                <a href="${pageContext.request.contextPath}/home">Quay lại cửa hàng</a>
            </div>
        </form>
    </section>
    </main>
</body>
</html>
