<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Không có quyền truy cập</title>
    <style>
        * { box-sizing: border-box; }
        body { margin: 0; min-height: 100vh; display: grid; place-items: center; background: #f4f6f9; color: #172033; font-family: Arial, sans-serif; }
        .notice { width: min(92vw, 560px); padding: 32px; background: #fff; border-radius: 8px; box-shadow: 0 18px 50px rgba(15,23,42,.12); text-align: center; }
        .code { display: inline-flex; align-items: center; justify-content: center; min-width: 76px; height: 42px; margin-bottom: 18px; border-radius: 6px; background: #fff4e5; color: #b45309; font-weight: 700; }
        h1 { margin: 0 0 12px; font-size: 24px; }
        p { margin: 0 0 22px; color: #475569; line-height: 1.6; }
        a { display: inline-flex; min-height: 42px; align-items: center; justify-content: center; padding: 0 18px; border-radius: 6px; background: #1477e8; color: #fff; font-weight: 700; text-decoration: none; }
    </style>
</head>
<body>
    <main class="notice">
        <div class="code">403</div>
        <h1>Không có quyền truy cập</h1>
        <c:choose>
            <c:when test="${requestScope.deniedReason == 'MANAGER_USER_MANAGEMENT'}">
                <p>Vì bạn là Manager nên bạn không có quyền vào chức năng Quản lý người dùng. Bạn vẫn có thể quản lý danh mục, sản phẩm, đơn hàng và thống kê.</p>
            </c:when>
            <c:otherwise>
                <p>Tài khoản hiện tại không có quyền mở trang này.</p>
            </c:otherwise>
        </c:choose>
        <a href="${pageContext.request.contextPath}${requestScope.returnPath}">Quay lại trang phù hợp</a>
    </main>
</body>
</html>
