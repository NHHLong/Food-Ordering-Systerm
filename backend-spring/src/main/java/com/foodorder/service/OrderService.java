package com.foodorder.service;

import com.foodorder.dto.CheckoutRequest;
import com.foodorder.dto.OrderStatusRequest;
import com.foodorder.model.Order;
import com.foodorder.model.OrderDetail;
import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    int placeOrder(CheckoutRequest request);
    List<Order> getOrders(Integer userId);
    List<OrderDetail> getOrderDetails(int orderId, int requesterId, String requesterRole);
    Order updateOrderStatus(int orderId, OrderStatusRequest request);
    BigDecimal getRevenue();
}
