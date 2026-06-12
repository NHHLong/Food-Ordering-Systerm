package com.foodorder.service.impl;

import com.foodorder.dao.ICartDAO;
import com.foodorder.dao.IFoodDAO;
import com.foodorder.dto.CartItemRequest;
import com.foodorder.model.Cart;
import com.foodorder.model.CartItem;
import com.foodorder.model.Food;
import com.foodorder.model.PersistentCartItem;
import com.foodorder.service.CartService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {
    private final ICartDAO cartDAO;
    private final IFoodDAO foodDAO;

    public CartServiceImpl(ICartDAO cartDAO, IFoodDAO foodDAO) {
        this.cartDAO = cartDAO;
        this.foodDAO = foodDAO;
    }

    @Override
    public List<CartItem> getCartItems(int userId) {
        return toCartItems(cartDAO.findItemsByUserId(userId));
    }

    @Override
    @Transactional
    public List<CartItem> addCartItem(CartItemRequest request) {
        Cart cart = cartDAO.findOrCreateByUserId(request.userId());
        Food food = foodDAO.findById(request.foodId()).orElseThrow(() -> new IllegalArgumentException("Food does not exist."));
        PersistentCartItem item = cartDAO.findItem(cart.getCartId(), request.foodId());
        if (item == null) {
            item = new PersistentCartItem();
            item.setCartId(cart.getCartId());
            item.setFoodId(food.getFoodId());
            item.setQuantity(Math.max(1, request.quantity()));
            item.setUnitPrice(food.getPrice());
        } else {
            item.setQuantity(item.getQuantity() + Math.max(1, request.quantity()));
            item.setUnitPrice(food.getPrice());
        }
        cartDAO.saveItem(item);
        return getCartItems(request.userId());
    }

    @Override
    @Transactional
    public List<CartItem> updateCartItem(CartItemRequest request) {
        Cart cart = cartDAO.findOrCreateByUserId(request.userId());
        PersistentCartItem item = cartDAO.findItem(cart.getCartId(), request.foodId());
        if (item != null) {
            item.setQuantity(Math.max(1, request.quantity()));
            cartDAO.saveItem(item);
        }
        return getCartItems(request.userId());
    }

    @Override
    @Transactional
    public List<CartItem> removeCartItem(int userId, int foodId) {
        Cart cart = cartDAO.findOrCreateByUserId(userId);
        cartDAO.removeItem(cart.getCartId(), foodId);
        return getCartItems(userId);
    }

    @Override
    @Transactional
    public void clearCart(int userId) {
        cartDAO.clearItems(userId);
    }

    private List<CartItem> toCartItems(List<PersistentCartItem> rows) {
        return rows.stream().map(row -> {
            Food food = foodDAO.findById(row.getFoodId()).orElse(new Food());
            CartItem item = new CartItem();
            item.setFood(food);
            item.setQuantity(row.getQuantity());
            return item;
        }).toList();
    }
}
