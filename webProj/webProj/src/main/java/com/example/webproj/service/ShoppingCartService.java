package com.example.webproj.service;

import java.util.Map;

public interface ShoppingCartService {
    int getCartCount(Integer shoppingCartId);
    Map<String, Object> updateCartItem(Integer userId, Integer productId, Integer count, Integer checked);
    Map<String, Object> clearCart(Integer userId);
    Map<String, Object> deleteCartItem(Integer userId, Integer productId);
    Map<String, Object> getCartItems(Integer userId);
    Map<String, Object> saveOrUpdateCartItem(Integer userId, Integer productId, Integer quantity);
}
