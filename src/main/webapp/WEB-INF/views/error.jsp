<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head><title>Lỗi</title></head>
<body>
    <h2 style="color: red;">Đã xảy ra lỗi hệ thống (HTTP 500). Vui lòng thử lại sau.</h2>
    <a href="${pageContext.request.contextPath}/login">Thử lại</a> | 
    <a href="${pageContext.request.contextPath}/admin/category/list">Về trang quản trị</a>
</body>
</html>