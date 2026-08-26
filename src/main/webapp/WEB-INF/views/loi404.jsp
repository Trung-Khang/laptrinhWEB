<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head><title>404 - Không tìm thấy</title></head>
<body>
    <h2 style="color: orange;">404 - Không tìm thấy trang!</h2>
    <p>Trang bạn yêu cầu không tồn tại hoặc đã bị di chuyển.</p>
    <a href="${pageContext.request.contextPath}/admin/category/list">Về trang quản trị</a> |
    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
</body>
</html>