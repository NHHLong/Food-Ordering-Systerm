package com.foodorder.controller;

import com.foodorder.model.Order;
import com.foodorder.model.OrderDetail;
import com.foodorder.service.OrderService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orders;

    public OrderController(OrderService orders) {
        this.orders = orders;
    }

    @GetMapping
    public List<Order> history(@RequestParam(required = false) Integer userId) {
        return orders.getOrders(userId);
    }

    @GetMapping("/{orderId}/details")
    public List<OrderDetail> details(
        @PathVariable int orderId,
        @RequestParam int userId,
        @RequestParam(required = false) String role
    ) {
        return orders.getOrderDetails(orderId, userId, role);
    }
}
