<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><title>Đăng nhập</title></head><body>
<h2>Đăng nhập</h2>
<c:if test="${param.registered == '1'}"><p style="color:green">Đăng ký thành công. Bạn có thể đăng nhập ngay.</p></c:if>
<c:if test="${not empty error}"><p style="color:red"><c:out value="${error}"/></p></c:if>
<form action="${pageContext.request.contextPath}/login" method="post">
  <label>Tên đăng nhập <input name="username" required value="${not empty username ? username : rememberedUsername}"></label><br><br>
  <label>Mật khẩu <input name="password" type="password" required></label><br><br>
  <label><input name="remember" type="checkbox" value="1"> Ghi nhớ tên đăng nhập</label><br><br>
  <button type="submit">Đăng nhập</button>
</form>
<p><a href="${pageContext.request.contextPath}/register">Chưa có tài khoản? Đăng ký</a></p>
</body></html>
