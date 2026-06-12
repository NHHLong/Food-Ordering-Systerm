package com.foodorder.dao;

import com.foodorder.model.Order;
import com.foodorder.model.OrderDetail;
import java.math.BigDecimal;
import java.util.List;

public interface IOrderDAO extends IGenericDAO<Order, Integer> {
    int createOrder(Order order);
    List<Order> findByUserId(Integer userId);
    List<OrderDetail> findDetailsByOrderId(int orderId);
    void updateStatus(int orderId, String status);
    BigDecimal getRevenue();
}
