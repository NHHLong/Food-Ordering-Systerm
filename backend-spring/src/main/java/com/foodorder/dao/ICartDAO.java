package com.foodorder.dao;

import com.foodorder.model.Cart;
import com.foodorder.model.CartItemEntity;
import java.util.List;

public interface ICartDAO extends IGenericDAO<Cart, Integer> {
    Cart findOrCreateByUserId(int userId);
    List<CartItemEntity> findItemsByUserId(int userId);
    CartItemEntity findItem(int cartId, int foodId);
    CartItemEntity saveItem(CartItemEntity item);
    void removeItem(int cartId, int foodId);
    void clearItems(int userId);
}
