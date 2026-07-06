package com.foodorder.dto;

import com.foodorder.model.CartItem;
import com.foodorder.model.PaymentMethod;
import com.foodorder.model.User;
import com.foodorder.model.Voucher;
import java.util.List;

public record CheckoutRequest(User user, List<CartItem> items, Voucher voucher, PaymentMethod paymentMethod, String shippingAddress, AddressRequest deliveryAddress, String note) {}
