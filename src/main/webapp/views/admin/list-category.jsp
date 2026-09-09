<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4">
    <div>
        <p class="text-primary text-uppercase fw-semibold small mb-1">Bài tập 03</p>
        <h2 class="h4 mb-0">Danh mục sản phẩm</h2>
    </div>
    <a href="${pageContext.request.contextPath}/admin/category/add" class="btn btn-primary"><i class="fa-solid fa-plus me-1"></i>Thêm danh mục</a>
</div>

<c:if test="${not empty param.message}"><div class="alert alert-success alert-dismissible fade show" role="alert"><c:out value="${param.message}"/><button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button></div></c:if>
<c:if test="${not empty param.error}"><div class="alert alert-danger alert-dismissible fade show" role="alert"><c:out value="${param.error}"/><button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Đóng"></button></div></c:if>

<section class="card shadow-sm">
    <div class="card-header bg-white d-flex flex-wrap justify-content-between align-items-center gap-3">
        <h3 class="h5 mb-0"><i class="fa-solid fa-layer-group text-primary me-2"></i>Danh sách danh mục</h3>
        <form action="${pageContext.request.contextPath}/admin/category/list" method="get" class="d-flex" role="search">
            <label class="visually-hidden" for="keyword">Tìm danh mục</label>
            <input id="keyword" name="keyword" value="${fn:escapeXml(param.keyword)}" class="form-control" placeholder="Tìm danh mục...">
            <button class="btn btn-outline-primary ms-2" type="submit" aria-label="Tìm kiếm"><i class="fa-solid fa-magnifying-glass"></i></button>
        </form>
    </div>
    <div class="table-responsive">
        <table class="table table-hover align-middle mb-0">
            <thead class="table-light"><tr><th scope="col">STT</th><th scope="col">Tên danh mục</th><th scope="col">Icon</th><th scope="col" class="text-end">Thao tác</th></tr></thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty cateList}"><tr><td colspan="4" class="text-center text-secondary py-5">Chưa có danh mục nào.</td></tr></c:when>
                    <c:otherwise>
                        <c:forEach var="cate" items="${cateList}" varStatus="status">
                            <tr>
                                <td>${status.count}</td>
                                <td class="fw-semibold"><c:out value="${cate.name}"/></td>
                                <td><c:choose><c:when test="${not empty cate.icon}"><img src="${pageContext.request.contextPath}/image?fname=${fn:escapeXml(cate.icon)}" alt="${fn:escapeXml(cate.name)}" class="rounded" style="width:56px;height:56px;object-fit:cover;"></c:when><c:otherwise><span class="text-secondary">Chưa có ảnh</span></c:otherwise></c:choose></td>
                                <td class="text-end text-nowrap">
                                    <a href="${pageContext.request.contextPath}/admin/category/edit?id=${cate.id}" class="btn btn-sm btn-outline-primary"><i class="fa-solid fa-pen me-1"></i>Sửa</a>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/category/delete" class="d-inline" onsubmit="return confirm('Bạn có chắc muốn xóa danh mục này?');"><input type="hidden" name="id" value="${cate.id}"><button class="btn btn-sm btn-outline-danger" type="submit"><i class="fa-solid fa-trash me-1"></i>Xóa</button></form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</section>
