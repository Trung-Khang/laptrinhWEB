<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Đăng ký tài khoản</title></head>
<body>
    <h2>Đăng Ký Tài Khoản</h2>

    <c:if test="${not empty error}">
        <div style="color: red; border: 1px solid red; padding: 8px; margin-bottom: 12px;">
            <strong>Lỗi:</strong> ${error}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post">
        <div>
            <label>Tên đăng nhập:</label>
            <input type="text" name="username" value="${username}" required />
        </div>
        <br/>
        <div>
            <label>Mật khẩu:</label>
            <input type="password" name="password" required />
        </div>
        <br/>
        <div>
            <label>Email:</label>
            <input type="email" name="email" value="${email}" required />
        </div>
        <br/>
        <div>
            <label>Họ và tên:</label>
            <input type="text" name="fullname" value="${fullname}" required />
        </div>
        <br/>
        <div>
            <label>Số điện thoại:</label>
            <input type="text" name="phone" value="${phone}" required />
        </div>
        <br/>
        <button type="submit">Đăng ký</button>
    </form>
    <br/>
    <a href="${pageContext.request.contextPath}/login">Đã có tài khoản? Đăng nhập</a>
</body>
</html>