package com.example.webproj.service;

import com.example.webproj.mappers.ShoppingCartMapper;
import com.example.webproj.pojo.ShoppingCartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper;

    public ShoppingCartServiceImpl(ShoppingCartMapper shoppingCartMapper) {
        this.shoppingCartMapper = shoppingCartMapper;
    }

    /**
     * 获取用户购物车商品数量
     * @param userId 用户ID（如果未登录，传入null）
     * @return 包含状态和数据的Map
     */
    public Map<String, Object> getCartCount(Integer userId) {
        Map<String, Object> result = new HashMap<>();

        if (userId == null) {
            // 用户未登录
            result.put("status", 1);
            result.put("msg", "请登录后，再查看购物车！");
            return result;
        }

        // 查询购物车商品数量（如果购物车为空，返回0）
        int count = shoppingCartMapper.getCartCountByUserId(userId);

        // 返回成功结果
        result.put("status", 0);
        result.put("data", count);
        return result;
    }

    /**
     * 更新购物车商品数量
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 新数量
     * @param checked 是否选中(0或1)
     * @return 操作结果
     */
    @Transactional
    public Map<String, Object> updateCartItem(
            Integer userId,
            Integer productId,
            Integer quantity,
            Integer checked
    ) {
        Map<String, Object> result = new HashMap<>();

        // 检查用户是否登录
        if (userId == null) {
            result.put("status", 1);
            result.put("msg", "请登录后，再查看购物车！");
            return result;
        }

        // 检查参数有效性
        if (productId == null || quantity == null || checked == null) {
            result.put("status", 1);
            result.put("msg", "参数不完整！");
            return result;
        }

        // 更新购物车商品
        int affectedRows = shoppingCartMapper.updateCartItem(userId, productId, quantity, checked);

        if (affectedRows == 0) {
            result.put("status", 1);
            result.put("msg", "更新失败，商品可能不存在于购物车中！");
            return result;
        }

        // 获取更新后的购物车列表
        List<ShoppingCartVo> cartItems = shoppingCartMapper.getCartItems(userId);

        // 计算总价(示例中返回0，实际应根据业务需求计算)
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ShoppingCartVo item : cartItems) {
            totalPrice = totalPrice.add(item.getTotalPrice());
        }

        // 构建成功响应
        Map<String, Object> data = new HashMap<>();
        data.put("lists", cartItems);
        data.put("totalPrice", totalPrice);

        result.put("status", 0);
        result.put("data", data);
        return result;
    }

    /**
     * 清空用户购物车
     * @param userId 用户ID
     * @return 操作结果
     */
    @Transactional
    public Map<String, Object> clearCart(Integer userId) {
        Map<String, Object> result = new HashMap<>();

        // 检查用户是否登录
        if (userId == null) {
            result.put("status", 1);
            result.put("msg", "请登录后，再查看购物车！");
            return result;
        }

        // 执行清空购物车操作
        int affectedRows = shoppingCartMapper.clearCart(userId);

        // 检查是否成功清空
        if (affectedRows >= 0) {
            result.put("status", 0);
            result.put("msg", "成功清空购物车！");
        } else {
            result.put("status", 1);
            result.put("msg", "清空购物车失败！");
        }

        return result;
    }

    /**
     * 从购物车中删除指定商品
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 操作结果
     */
    @Transactional
    public Map<String, Object> deleteCartItem(Integer userId, Integer productId) {
        Map<String, Object> result = new HashMap<>();

        // 检查用户是否登录
        if (userId == null) {
            result.put("status", 1);
            result.put("msg", "请登录后，再查看购物车！");
            return result;
        }

        // 检查参数是否有效
        if (productId == null) {
            result.put("status", 1);
            result.put("msg", "商品ID不能为空！");
            return result;
        }

        // 执行删除操作
        int affectedRows = shoppingCartMapper.deleteCartItem(userId, productId);

        // 检查是否成功删除
        if (affectedRows == 0) {
            result.put("status", 1);
            result.put("msg", "商品删除失败！商品可能不存在于购物车中。");
            return result;
        }

        // 获取更新后的购物车列表
        List<ShoppingCartVo> cartItems = shoppingCartMapper.getCartItems(userId);

        // 计算整个购物车的总金额
        BigDecimal cartTotalPrice = calculateCartTotalPrice(cartItems);

        // 构建成功响应
        Map<String, Object> data = new HashMap<>();
        data.put("lists", cartItems);
        data.put("totalPrice", cartTotalPrice);

        result.put("status", 0);
        result.put("data", data);
        return result;
    }

    /**
     * 计算购物车总金额
     * @param cartItems 购物车商品列表
     * @return 总金额
     */
    private BigDecimal calculateCartTotalPrice(List<ShoppingCartVo> cartItems) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ShoppingCartVo item : cartItems) {
            totalPrice = totalPrice.add(item.getTotalPrice());
        }
        return totalPrice;
    }

    /**
     * 获取用户购物车商品列表及总价格
     * @param userId 用户ID
     * @return 操作结果
     */
    public Map<String, Object> getCartItems(Integer userId) {
        Map<String, Object> result = new HashMap<>();

        // 检查用户是否登录
        if (userId == null) {
            result.put("status", 1);
            result.put("msg", "请登录后，再查看购物车！");
            return result;
        }

        // 获取购物车商品列表
        List<ShoppingCartVo> cartItems = shoppingCartMapper.getCartItems(userId);

        // 计算整个购物车的总金额
        BigDecimal cartTotalPrice = calculateCartTotalPrice(cartItems);

        // 构建成功响应
        Map<String, Object> data = new HashMap<>();
        data.put("lists", cartItems);
        data.put("totalPrice", cartTotalPrice);

        result.put("status", 0);
        result.put("data", data);
        return result;
    }

    /**
     * 添加商品到购物车
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 商品数量
     * @return 操作结果
     */
    @Transactional
    public Map<String, Object> saveOrUpdateCartItem(Integer userId, Integer productId, Integer quantity) {
        Map<String, Object> result = new HashMap<>();

        // 检查用户是否登录
        if (userId == null) {
            result.put("status", 1);
            result.put("msg", "请登录后，再查看购物车！");
            return result;
        }

        // 检查参数是否有效
        if (productId == null || quantity == null) {
            result.put("status", 1);
            result.put("msg", "商品ID和数量不能为空！");
            return result;
        }

        // 检查数量是否有效
        if (quantity <= 0) {
            result.put("status", 1);
            result.put("msg", "商品数量必须大于0！");
            return result;
        }

        // 执行添加或更新操作
        int affectedRows = shoppingCartMapper.saveOrUpdateCartItem(userId, productId, quantity);

        // 检查是否成功操作
        if (affectedRows > 0) {
            result.put("status", 0);
            result.put("msg", "商品已成功加入购物车！");
        } else {
            result.put("status", 1);
            result.put("msg", "商品添加失败！");
        }

        return result;
    }

}
