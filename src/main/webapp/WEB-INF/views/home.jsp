<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head><meta charset="UTF-8"><title>Trang chủ</title></head>
<body>
    <h2>Xin chào, ${sessionScope.account.fullName}</h2>
    <p>Bạn đã đăng nhập thành công với tài khoản ${sessionScope.account.userName}.</p>
    <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
</body>
</html>
