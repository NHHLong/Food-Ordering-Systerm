package com.foodorder.dao;

import com.foodorder.model.Cart;
import com.foodorder.model.PersistentCartItem;
import java.util.List;

public interface ICartDAO extends IGenericDAO<Cart, Integer> {
    Cart findOrCreateByUserId(int userId);
    List<PersistentCartItem> findItemsByUserId(int userId);
    PersistentCartItem findItem(int cartId, int foodId);
    PersistentCartItem saveItem(PersistentCartItem item);
    void removeItem(int cartId, int foodId);
    void clearItems(int userId);
}
