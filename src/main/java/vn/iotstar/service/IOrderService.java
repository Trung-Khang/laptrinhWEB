package vn.iotstar.service;

import java.util.List;

import vn.iotstar.entity.Order;

public interface IOrderService {
    String[] STATUSES = {"PENDING", "CONFIRMED", "SHIPPING", "COMPLETED", "CANCELLED"};

    List<Order> search(String keyword, String status, int page, int pageSize);
    long count(String keyword, String status);
    Order findById(int id);
    void updateStatus(int id, String newStatus);
}
