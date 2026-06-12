package com.foodorder.service.impl;

import com.foodorder.dao.IAddressDAO;
import com.foodorder.dao.IOrderDAO;
import com.foodorder.dto.CheckoutRequest;
import com.foodorder.dto.NotificationCreateRequest;
import com.foodorder.dto.OrderStatusRequest;
import com.foodorder.model.Address;
import com.foodorder.model.CartItem;
import com.foodorder.model.Order;
import com.foodorder.model.OrderDetail;
import com.foodorder.model.PaymentMethod;
import com.foodorder.model.Voucher;
import com.foodorder.service.OrderService;
import com.foodorder.service.VoucherService;
import com.foodorder.service.CartService;
import com.foodorder.service.NotificationService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    private final IOrderDAO orderDAO;
    private final IAddressDAO addressDAO;
    private final VoucherService voucherService;
    private final CartService cartService;
    private final NotificationService notificationService;

    public OrderServiceImpl(IOrderDAO orderDAO, IAddressDAO addressDAO, VoucherService voucherService, CartService cartService, NotificationService notificationService) {
        this.orderDAO = orderDAO;
        this.addressDAO = addressDAO;
        this.voucherService = voucherService;
        this.cartService = cartService;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public int placeOrder(CheckoutRequest request) {
        if (request.user() == null || request.user().getUserId() <= 0) {
            throw new IllegalArgumentException("Please login before checkout.");
        }
        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty.");
        }
        BigDecimal total = request.items().stream()
            .map(CartItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        Voucher voucher = request.voucher();
        if (voucher != null) {
            if (voucher.getDiscountPercent() != null) {
                total = total.subtract(total.multiply(BigDecimal.valueOf(voucher.getDiscountPercent())).divide(BigDecimal.valueOf(100)));
            }
            if (voucher.getDiscountAmount() != null) {
                total = total.subtract(voucher.getDiscountAmount());
            }
            if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;
        }

        Order order = new Order();
        order.setUserId(request.user().getUserId());
        order.setVoucherId(voucher == null ? null : voucher.getVoucherId());
        Address address = null;
        if (request.deliveryAddress() != null) {
            address = new Address();
            address.setUserId(request.user().getUserId());
            address.setCity(request.deliveryAddress().city());
            address.setStreet(request.deliveryAddress().street());
            address.setBuildingNumber(request.deliveryAddress().buildingNumber());
            addressDAO.save(address);
            order.setAddressId(address.getAddressId());
        }
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(total);
        order.setStatus("Pending");
        order.setPaymentMethod(request.paymentMethod() == null ? PaymentMethod.Cash : request.paymentMethod());
        order.setShippingAddress(address != null ? address.getFullAddress() : request.shippingAddress());
        order.setDetails(request.items().stream().map(item -> {
            OrderDetail detail = new OrderDetail();
            detail.setFoodId(item.getFood().getFoodId());
            detail.setQuantity(item.getQuantity());
            detail.setUnitPrice(item.getFood().getPrice());
            return detail;
        }).toList());

        int orderId = orderDAO.createOrder(order);
        if (voucher != null) voucherService.markVoucherUsed(voucher.getVoucherId());
        cartService.clearCart(request.user().getUserId());
        notificationService.createNotification(new NotificationCreateRequest(
            request.user().getUserId(),
            "Order placed",
            "Your order #" + orderId + " has been placed successfully."
        ));
        return orderId;
    }

    @Override
    public List<Order> getOrders(Integer userId) {
        return orderDAO.findByUserId(userId);
    }

    @Override
    public List<OrderDetail> getOrderDetails(int orderId) {
        return orderDAO.findDetailsByOrderId(orderId);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(int orderId, OrderStatusRequest request) {
        orderDAO.updateStatus(orderId, request.status());
        Order order = orderDAO.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order does not exist."));
        notificationService.createNotification(new NotificationCreateRequest(
            order.getUserId(),
            "Order status updated",
            "Order #" + orderId + " status changed to " + request.status() + "."
        ));
        return order;
    }

    @Override
    public BigDecimal getRevenue() {
        return orderDAO.getRevenue();
    }
}
