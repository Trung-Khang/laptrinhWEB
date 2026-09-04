package vn.iotstar.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.IOrderService;
import vn.iotstar.service.impl.OrderServiceImpl;

@WebServlet(urlPatterns = "/admin/order/update-status")
public class OrderUpdateStatusController extends OrderBaseController {
    private static final long serialVersionUID = 1L;
    private final IOrderService orderService = new OrderServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Integer id = parseInteger(req.getParameter("id"));
        String next = trim(req.getParameter("status"));
        String back = req.getContextPath() + "/admin/order/detail?id=" + id;
        try {
            if (id == null) throw new IllegalArgumentException("Mã đơn hàng không hợp lệ.");
            orderService.updateStatus(id, next);
            resp.sendRedirect(back + "&message=" + URLEncoder.encode("Cập nhật trạng thái thành công", StandardCharsets.UTF_8));
        } catch (Exception e) {
            resp.sendRedirect(back + "&error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }
}
