<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html lang="vi"><head><meta charset="UTF-8"><title>Đăng ký</title></head><body>
<h2>Đăng ký tài khoản</h2>
<c:if test="${not empty error}"><p style="color:red"><c:out value="${error}"/></p></c:if>
<form action="${pageContext.request.contextPath}/register" method="post">
  <label>Tên đăng nhập <input name="username" required value="${formUser.userName}"></label><br><br>
  <label>Mật khẩu <input name="password" type="password" required></label><br><br>
  <label>Email <input name="email" type="email" required value="${formUser.email}"></label><br><br>
  <label>Họ và tên <input name="fullname" value="${formUser.fullName}"></label><br><br>
  <label>Số điện thoại <input name="phone" value="${formUser.phone}"></label><br><br>
  <button type="submit">Đăng ký</button>
</form>
<p><a href="${pageContext.request.contextPath}/login">Đã có tài khoản? Đăng nhập</a></p>
</body></html>
