package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.dao.StatisticsDao;

@WebServlet(urlPatterns = "/admin/statistics")
public class StatisticsController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final StatisticsDao dao = new StatisticsDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("totalProducts", dao.totalProducts());
        req.setAttribute("totalStock", dao.totalStock());
        req.setAttribute("lowStock", dao.lowStock());
        req.setAttribute("outOfStock", dao.outOfStock());
        req.setAttribute("inventoryValue", dao.inventoryValue());
        req.setAttribute("productByCategory", dao.productByCategory());
        req.setAttribute("lowStockProducts", dao.lowStockProducts());
        req.setAttribute("topSellingProducts", dao.topSellingProducts());
        req.getRequestDispatcher("/views/admin/statistics.jsp").forward(req, resp);
    }
}
