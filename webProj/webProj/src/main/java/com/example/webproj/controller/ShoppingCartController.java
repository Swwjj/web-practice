package com.example.webproj.controller;

import com.example.webproj.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/actionmall/cart")
@CrossOrigin(origins = "*")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/getcartcount.do")
    public Map<String, Object> getCartCount(HttpSession session) {
        // 从Session中获取用户ID（假设登录后存储在"userId"属性中）
        Integer userId = (Integer) session.getAttribute("userId");

        // 调用Service获取购物车数量
        return shoppingCartService.getCartCount(userId);
    }

    @GetMapping("/updatecarts.do")
    public Map<String, Object> updateCart(
            HttpSession session,
            @RequestParam("productId") String productIdStr,
            @RequestParam("count") String countStr,
            @RequestParam("checked") String checkedStr
    ) {
        // 从Session中获取用户ID
        Integer userId = (Integer) session.getAttribute("userId");

        // 参数转换
        Integer productId = null;
        Integer count = null;
        Integer checked = null;

        try {
            productId = Integer.parseInt(productIdStr);
            count = Integer.parseInt(countStr);
            checked = Integer.parseInt(checkedStr);
        } catch (NumberFormatException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", 1);
            result.put("msg", "参数格式错误！");
            return result;
        }

        // 调用Service更新购物车
        return shoppingCartService.updateCartItem(userId, productId, count, checked);
    }

    @GetMapping("/clearcarts.do")
    public Map<String, Object> clearCart(HttpSession session) {
        // 从Session中获取用户ID
        Integer userId = (Integer) session.getAttribute("userId");

        // 调用Service清空购物车
        return shoppingCartService.clearCart(userId);
    }

    @PostMapping("/delcarts.do")
    public Map<String, Object> deleteCartItem(
            HttpSession session,
            @RequestParam("productId") String productIdStr
    ) {
        // 从Session中获取用户ID
        Integer userId = (Integer) session.getAttribute("userId");

        // 参数转换
        Integer productId = null;

        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", 1);
            result.put("msg", "商品ID格式错误！");
            return result;
        }

        // 调用Service删除购物车商品
        return shoppingCartService.deleteCartItem(userId, productId);
    }

    @GetMapping("/findallcarts.do")
    public Map<String, Object> findAllCarts(HttpSession session) {
        // 从Session中获取用户ID
        Integer userId = (Integer) session.getAttribute("userId");

        // 调用Service获取购物车商品列表
        return shoppingCartService.getCartItems(userId);
    }

    @PostMapping("/savecart.do")
    public Map<String, Object> saveCart(
            HttpSession session,
            @RequestParam("productId") String productIdStr,
            @RequestParam("count") String countStr
    ) {
        // 从Session中获取用户ID
        Integer userId = (Integer) session.getAttribute("userId");

        // 参数转换
        Integer productId = null;
        Integer quantity = null;

        try {
            productId = Integer.parseInt(productIdStr);
            quantity = Integer.parseInt(countStr);
        } catch (NumberFormatException e) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", 1);
            result.put("msg", "商品ID或数量格式错误！");
            return result;
        }

        // 调用Service添加商品到购物车
        return shoppingCartService.saveOrUpdateCartItem(userId, productId, quantity);
    }
}
