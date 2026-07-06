package com.foodorder.dao;

import com.foodorder.model.Address;
import com.foodorder.model.Order;
import com.foodorder.model.OrderDetail;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDAOImpl extends GenericDAOImpl<Order, Integer> implements IOrderDAO {
    public OrderDAOImpl() {
        super(Order.class);
    }

    @Override
    public int createOrder(Order order) {
        save(order);
        for (OrderDetail detail : order.getDetails()) {
            detail.setOrderId(order.getOrderId());
            entityManager.persist(detail);
        }
        entityManager.flush();
        return order.getOrderId();
    }

    @Override
    public List<Order> findByUserId(Integer userId) {
        String jpql = userId == null
            ? "SELECT o FROM OrderEntity o ORDER BY o.orderDate DESC"
            : "SELECT o FROM OrderEntity o WHERE o.userId = :userId ORDER BY o.orderDate DESC";
        var query = entityManager.createQuery(jpql, Order.class);
        if (userId != null) {
            query.setParameter("userId", userId);
        }
        List<Order> orders = query.getResultList();
        fillUsernames(orders);
        fillAddresses(orders);
        return orders;
    }

    @Override
    public List<Order> findAll() {
        return findByUserId(null);
    }

    @Override
    public List<OrderDetail> findDetailsByOrderId(int orderId) {
        List<OrderDetail> details = entityManager
            .createQuery("SELECT d FROM OrderDetail d WHERE d.orderId = :orderId", OrderDetail.class)
            .setParameter("orderId", orderId)
            .getResultList();
        for (OrderDetail detail : details) {
            String foodName = entityManager
                .createQuery("SELECT f.name FROM Food f WHERE f.foodId = :foodId", String.class)
                .setParameter("foodId", detail.getFoodId())
                .getResultStream()
                .findFirst()
                .orElse("");
            detail.setFoodName(foodName);
        }
        return details;
    }

    @Override
    public void updateStatus(int orderId, String status) {
        findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            update(order);
        });
    }

    @Override
    public BigDecimal getRevenue() {
        BigDecimal revenue = entityManager
            .createQuery("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o", BigDecimal.class)
            .getSingleResult();
        return revenue == null ? BigDecimal.ZERO : revenue;
    }

    private void fillUsernames(List<Order> orders) {
        for (Order order : orders) {
            String username = entityManager
                .createQuery("SELECT u.username FROM UserEntity u WHERE u.userId = :userId", String.class)
                .setParameter("userId", order.getUserId())
                .getResultStream()
                .findFirst()
                .orElse("");
            order.setUsername(username);
        }
    }

    private void fillAddresses(List<Order> orders) {
        for (Order order : orders) {
            if (order.getAddressId() == null) continue;
            entityManager
                .createQuery("SELECT a FROM Address a WHERE a.addressId = :addressId", Address.class)
                .setParameter("addressId", order.getAddressId())
                .getResultStream()
                .findFirst()
                .ifPresent(order::setAddress);
        }
    }
}
