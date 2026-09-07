<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Hồ sơ cá nhân | KhangGear</title>
</head>
<body>
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

        <form method="post" action="${pageContext.request.contextPath}/profile" class="profile-form">
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
            <p class="avatar-note">Upload ảnh đại diện sẽ được bổ sung ở Giai đoạn 3. Hiện tại hệ thống chỉ hiển thị avatar đã có trong dữ liệu.</p>
            <div class="form-actions">
                <button type="submit"><i class="fa-solid fa-floppy-disk"></i> Lưu thay đổi</button>
                <a href="${pageContext.request.contextPath}/home">Quay lại cửa hàng</a>
            </div>
        </form>
    </section>
</body>
</html>
