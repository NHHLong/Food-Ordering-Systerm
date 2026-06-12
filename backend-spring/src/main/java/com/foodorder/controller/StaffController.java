package com.foodorder.controller;

import com.foodorder.dto.OrderStatusRequest;
import com.foodorder.model.Notification;
import com.foodorder.model.Order;
import com.foodorder.model.SupportRequest;
import com.foodorder.service.NotificationService;
import com.foodorder.service.OrderService;
import com.foodorder.service.SupportService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
    private final OrderService orders;
    private final SupportService support;
    private final NotificationService notifications;

    public StaffController(OrderService orders, SupportService support, NotificationService notifications) {
        this.orders = orders;
        this.support = support;
        this.notifications = notifications;
    }

    @GetMapping("/orders")
    public List<Order> orders() {
        return orders.getOrders(null);
    }

    @PatchMapping("/orders/{orderId}/status")
    public Order updateOrderStatus(@PathVariable int orderId, @RequestBody OrderStatusRequest request) {
        return orders.updateOrderStatus(orderId, request);
    }

    @GetMapping("/support")
    public List<SupportRequest> supportRequests() {
        return support.getAllSupportRequests();
    }

    @GetMapping("/notifications")
    public List<Notification> notifications() {
        return notifications.getNotifications(null);
    }
}
