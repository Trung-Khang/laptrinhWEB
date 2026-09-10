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
            <a href="<%=contextPath%>/account/orders">Đơn hàng</a>
        </nav>
        <div class="profile-account">
            <c:choose>
                <c:when test="${not empty avatarUrl}">
                    <img src="${avatarUrl}" alt="Avatar" class="header-avatar-circle" onerror="this.style.display='none'; this.nextElementSibling.style.display='inline-block';">
                    <i class="fa-solid fa-circle-user" style="display: none; font-size: 24px; color: #64748b;"></i>
                </c:when>
                <c:otherwise>
                    <i class="fa-solid fa-circle-user" style="font-size: 24px; color: #64748b;"></i>
                </c:otherwise>
            </c:choose>
            <span class="user-greeting"><c:out value="${sessionScope.account.fullName}" default="${sessionScope.account.userName}"/></span>
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
                    <c:when test="${not empty avatarUrl}">
                        <img src="${avatarUrl}" alt="Ảnh đại diện" onerror="this.style.display='none'; this.nextElementSibling.style.display='inline-block';">
                        <i class="fa-solid fa-user" style="display: none;"></i>
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
                <c:if test="${not empty fieldErrors.fullname}"><small class="field-error"><c:out value="${fieldErrors.fullname}"/></small></c:if>
            </label>
            <label>
                Số điện thoại
                <input name="phone" maxlength="30" value="${profileUser.phone}">
                <c:if test="${not empty fieldErrors.phone}"><small class="field-error"><c:out value="${fieldErrors.phone}"/></small></c:if>
            </label>
            <label>
                Ảnh đại diện mới
                <input type="file" name="avatar" accept="image/jpeg,image/png,image/webp">
                <c:if test="${not empty fieldErrors.avatar}"><small class="field-error"><c:out value="${fieldErrors.avatar}"/></small></c:if>
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
