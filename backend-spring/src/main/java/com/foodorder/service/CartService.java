package com.foodorder.service;

import com.foodorder.dto.CartItemRequest;
import com.foodorder.model.CartItem;
import java.util.List;

public interface CartService {
    List<CartItem> getCartItems(int userId);
    List<CartItem> addCartItem(CartItemRequest request);
    List<CartItem> updateCartItem(CartItemRequest request);
    List<CartItem> removeCartItem(int userId, int foodId);
    void clearCart(int userId);
}
