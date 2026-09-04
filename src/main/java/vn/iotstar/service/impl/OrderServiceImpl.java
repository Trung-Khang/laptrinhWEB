package vn.iotstar.service.impl;

import java.util.Arrays;
import java.util.List;

import vn.iotstar.dao.IOrderDao;
import vn.iotstar.dao.OrderDao;
import vn.iotstar.entity.Order;
import vn.iotstar.service.IOrderService;

public class OrderServiceImpl implements IOrderService {
    private final IOrderDao orderDao = new OrderDao();

    @Override
    public List<Order> search(String keyword, String status, int page, int pageSize) {
        return orderDao.search(keyword, normalizeStatus(status), Math.max(page, 1), pageSize);
    }

    @Override
    public long count(String keyword, String status) {
        return orderDao.count(keyword, normalizeStatus(status));
    }

    @Override
    public Order findById(int id) {
        return orderDao.findById(id);
    }

    @Override
    public void updateStatus(int id, String newStatus) {
        newStatus = normalizeStatus(newStatus);
        if (newStatus == null) throw new IllegalArgumentException("Trạng thái đơn hàng không hợp lệ.");
        Order order = orderDao.findById(id);
        if (order == null) throw new IllegalArgumentException("Không tìm thấy đơn hàng.");
        if (!canMove(order.getStatus(), newStatus)) {
            throw new IllegalStateException("Không thể chuyển trạng thái từ " + label(order.getStatus()) + " sang " + label(newStatus) + ".");
        }
        orderDao.updateStatus(id, newStatus);
    }

    private boolean canMove(String current, String next) {
        if (current == null || current.equals(next)) return true;
        if ("CANCELLED".equals(current) || "COMPLETED".equals(current)) return false;
        if ("CANCELLED".equals(next)) return !"COMPLETED".equals(current);
        if ("PENDING".equals(current)) return "CONFIRMED".equals(next);
        if ("CONFIRMED".equals(current)) return "SHIPPING".equals(next);
        if ("SHIPPING".equals(current)) return "COMPLETED".equals(next);
        return false;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.trim().isEmpty()) return null;
        String value = status.trim().toUpperCase();
        return Arrays.asList(STATUSES).contains(value) ? value : null;
    }

    private String label(String status) {
        if ("PENDING".equals(status)) return "Chờ xác nhận";
        if ("CONFIRMED".equals(status)) return "Đã xác nhận";
        if ("SHIPPING".equals(status)) return "Đang giao";
        if ("COMPLETED".equals(status)) return "Hoàn thành";
        if ("CANCELLED".equals(status)) return "Đã hủy";
        return status;
    }
}
