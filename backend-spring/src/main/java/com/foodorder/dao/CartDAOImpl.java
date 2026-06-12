package com.foodorder.dao;

import com.foodorder.model.Cart;
import com.foodorder.model.PersistentCartItem;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CartDAOImpl extends GenericDAOImpl<Cart, Integer> implements ICartDAO {
    public CartDAOImpl() {
        super(Cart.class);
    }

    @Override
    public Cart findOrCreateByUserId(int userId) {
        List<Cart> carts = entityManager
            .createQuery("SELECT c FROM Cart c WHERE c.userId = :userId ORDER BY c.cartId", Cart.class)
            .setParameter("userId", userId)
            .getResultList();
        if (!carts.isEmpty()) return carts.get(0);
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setCreatedAt(LocalDateTime.now());
        return save(cart);
    }

    @Override
    public List<PersistentCartItem> findItemsByUserId(int userId) {
        Cart cart = findOrCreateByUserId(userId);
        return entityManager
            .createQuery("SELECT i FROM PersistentCartItem i WHERE i.cartId = :cartId ORDER BY i.cartItemId", PersistentCartItem.class)
            .setParameter("cartId", cart.getCartId())
            .getResultList();
    }

    @Override
    public PersistentCartItem findItem(int cartId, int foodId) {
        return entityManager
            .createQuery("SELECT i FROM PersistentCartItem i WHERE i.cartId = :cartId AND i.foodId = :foodId", PersistentCartItem.class)
            .setParameter("cartId", cartId)
            .setParameter("foodId", foodId)
            .getResultStream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public PersistentCartItem saveItem(PersistentCartItem item) {
        if (item.getCartItemId() == 0) {
            entityManager.persist(item);
            entityManager.flush();
            return item;
        }
        return entityManager.merge(item);
    }

    @Override
    public void removeItem(int cartId, int foodId) {
        PersistentCartItem item = findItem(cartId, foodId);
        if (item != null) entityManager.remove(item);
    }

    @Override
    public void clearItems(int userId) {
        Cart cart = findOrCreateByUserId(userId);
        entityManager
            .createQuery("DELETE FROM PersistentCartItem i WHERE i.cartId = :cartId")
            .setParameter("cartId", cart.getCartId())
            .executeUpdate();
    }
}
