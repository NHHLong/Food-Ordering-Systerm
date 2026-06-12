package com.foodorder.controller;

import com.foodorder.dto.CheckoutRequest;
import com.foodorder.dto.CartItemRequest;
import com.foodorder.model.CartItem;
import com.foodorder.dto.VoucherRequest;
import com.foodorder.model.Voucher;
import com.foodorder.service.CartService;
import com.foodorder.service.OrderService;
import com.foodorder.service.VoucherService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cart;
    private final VoucherService vouchers;
    private final OrderService orders;

    public CartController(CartService cart, VoucherService vouchers, OrderService orders) {
        this.cart = cart;
        this.vouchers = vouchers;
        this.orders = orders;
    }

    @GetMapping
    public List<CartItem> items(@RequestParam int userId) {
        return cart.getCartItems(userId);
    }

    @PostMapping("/items")
    public List<CartItem> addItem(@RequestBody CartItemRequest request) {
        return cart.addCartItem(request);
    }

    @PutMapping("/items")
    public List<CartItem> updateItem(@RequestBody CartItemRequest request) {
        return cart.updateCartItem(request);
    }

    @DeleteMapping("/items")
    public List<CartItem> removeItem(@RequestParam int userId, @RequestParam int foodId) {
        return cart.removeCartItem(userId, foodId);
    }

    @DeleteMapping
    public void clear(@RequestParam int userId) {
        cart.clearCart(userId);
    }

    @PostMapping("/voucher")
    public Voucher applyVoucher(@RequestBody VoucherRequest request) {
        return vouchers.applyVoucher(request.code(), request.orderTotal());
    }

    @PostMapping("/checkout")
    public int checkout(@RequestBody CheckoutRequest request) {
        return orders.placeOrder(request);
    }
}
