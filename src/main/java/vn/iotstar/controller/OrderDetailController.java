package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Order;
import vn.iotstar.service.IOrderService;
import vn.iotstar.service.impl.OrderServiceImpl;

@WebServlet(urlPatterns = "/admin/order/detail")
public class OrderDetailController extends OrderBaseController {
    private static final long serialVersionUID = 1L;
    private final IOrderService orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = parseInteger(req.getParameter("id"));
        Order order = id == null ? null : orderService.findById(id);
        req.setAttribute("order", order);
        req.setAttribute("statuses", IOrderService.STATUSES);
        req.getRequestDispatcher("/views/admin/detail-order.jsp").forward(req, resp);
    }
}
