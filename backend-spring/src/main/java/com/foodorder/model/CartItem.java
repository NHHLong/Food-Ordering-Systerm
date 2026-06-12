package com.foodorder.model;

import java.math.BigDecimal;

public class CartItem {
    private Food food = new Food();
    private int quantity;

    public Food getFood() { return food; }
    public void setFood(Food food) { this.food = food; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getTotalPrice() {
        return food.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
