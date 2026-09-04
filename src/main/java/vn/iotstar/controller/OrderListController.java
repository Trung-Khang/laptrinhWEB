package vn.iotstar.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Order;
import vn.iotstar.service.IOrderService;
import vn.iotstar.service.impl.OrderServiceImpl;

@WebServlet(urlPatterns = "/admin/order/list")
public class OrderListController extends OrderBaseController {
    private static final long serialVersionUID = 1L;
    private final IOrderService orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String keyword = trim(req.getParameter("keyword"));
        String status = trim(req.getParameter("status"));
        Integer pageParam = parseInteger(req.getParameter("page"));
        int page = pageParam == null ? 1 : pageParam;

        List<Order> orders = orderService.search(keyword, status, page, PAGE_SIZE);
        long totalItems = orderService.count(keyword, status);
        int totalPages = (int) Math.ceil(totalItems * 1.0 / PAGE_SIZE);

        req.setAttribute("orders", orders);
        req.setAttribute("statuses", IOrderService.STATUSES);
        req.setAttribute("page", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalItems", totalItems);
        req.getRequestDispatcher("/views/admin/list-order.jsp").forward(req, resp);
    }
}
