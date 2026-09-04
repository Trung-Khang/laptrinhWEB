package vn.iotstar.dao;

import java.util.List;

import vn.iotstar.entity.Order;

public interface IOrderDao {
    List<Order> search(String keyword, String status, int page, int pageSize);
    long count(String keyword, String status);
    Order findById(int id);
    void updateStatus(int id, String status);
}
