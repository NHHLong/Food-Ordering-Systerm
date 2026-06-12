package com.foodorder.controller;

import com.foodorder.dto.CategorySaveRequest;
import com.foodorder.dto.DashboardResponse;
import com.foodorder.dto.FoodSaveRequest;
import com.foodorder.dto.OrderStatusRequest;
import com.foodorder.dto.UserActiveRequest;
import com.foodorder.dto.UserRoleRequest;
import com.foodorder.dto.VoucherSaveRequest;
import com.foodorder.model.Category;
import com.foodorder.model.Food;
import com.foodorder.model.Order;
import com.foodorder.model.SupportRequest;
import com.foodorder.model.User;
import com.foodorder.model.Voucher;
import com.foodorder.service.CategoryService;
import com.foodorder.service.FoodService;
import com.foodorder.service.OrderService;
import com.foodorder.service.SupportService;
import com.foodorder.service.UserService;
import com.foodorder.service.VoucherService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final FoodService foods;
    private final CategoryService categories;
    private final UserService users;
    private final OrderService orders;
    private final VoucherService vouchers;
    private final SupportService support;

    public AdminController(FoodService foods, CategoryService categories, UserService users, OrderService orders, VoucherService vouchers, SupportService support) {
        this.foods = foods;
        this.categories = categories;
        this.users = users;
        this.orders = orders;
        this.vouchers = vouchers;
        this.support = support;
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard() {
        List<Order> allOrders = orders.getOrders(null);
        return new DashboardResponse(orders.getRevenue(), allOrders.size());
    }

    @GetMapping("/foods")
    public List<Food> foods() { return foods.getAllFoods(null, null); }

    @PostMapping("/foods")
    public Food saveFood(@RequestBody FoodSaveRequest request) { return foods.saveFood(request); }

    @DeleteMapping("/foods/{foodId}")
    public void deleteFood(@PathVariable int foodId) { foods.deleteFoodById(foodId); }

    @GetMapping("/categories")
    public List<Category> categories() { return categories.getAllCategories(false); }

    @PostMapping("/categories")
    public Category saveCategory(@RequestBody CategorySaveRequest request) { return categories.saveCategory(request); }

    @GetMapping("/users")
    public List<User> users() { return users.getAllUsers(); }

    @PatchMapping("/users/{userId}/active")
    public User setUserActive(@PathVariable int userId, @RequestBody UserActiveRequest request) {
        return users.updateUserActive(userId, request);
    }

    @PatchMapping("/users/{userId}/role")
    public User setUserRole(@PathVariable int userId, @RequestBody UserRoleRequest request) {
        return users.updateUserRole(userId, request);
    }

    @GetMapping("/orders")
    public List<Order> orders() { return orders.getOrders(null); }

    @PatchMapping("/orders/{orderId}/status")
    public Order updateOrderStatus(@PathVariable int orderId, @RequestBody OrderStatusRequest request) {
        return orders.updateOrderStatus(orderId, request);
    }

    @GetMapping("/vouchers")
    public List<Voucher> vouchers() { return vouchers.getAllVouchers(); }

    @PostMapping("/vouchers")
    public Voucher saveVoucher(@RequestBody VoucherSaveRequest request) { return vouchers.saveVoucher(request); }

    @GetMapping("/support")
    public List<SupportRequest> supportRequests() { return support.getAllSupportRequests(); }
}
