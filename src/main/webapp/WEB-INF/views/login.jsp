<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head><title>Đăng nhập</title></head>
<body>
    <h2>Đăng Nhập</h2>

    <c:if test="${not empty error}">
        <div style="color: red; border: 1px solid red; padding: 8px; margin-bottom: 12px;">
            <strong>Lỗi:</strong> ${error}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
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
            <label>
                <input type="checkbox" name="remember" value="on" />
                Ghi nhớ đăng nhập (Remember Me)
            </label>
        </div>
        <br/>
        <button type="submit">Đăng nhập</button>
    </form>
    <br/>
    <a href="${pageContext.request.contextPath}/register">Chưa có tài khoản? Đăng ký</a>
</body>
</html>