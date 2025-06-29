package com.example.webproj.mappers;

import com.example.webproj.pojo.ShoppingCartVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 查询当前用户购物车中商品数量
     * @param userId 用户ID
     * @return 商品数量
     */
    Integer getCartCountByUserId(@Param("userId") Integer userId);

    /**
     * 更新购物车中商品数量
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 新数量
     * @param checked 是否选中(0或1)
     * @return 影响的行数
     */
    int updateCartItem(
            @Param("userId") Integer userId,
            @Param("productId") Integer productId,
            @Param("quantity") Integer quantity,
            @Param("checked") Integer checked
    );

    /**
     * 获取用户购物车中的商品列表
     * @param userId 用户ID
     * @return 商品列表
     */
    List<ShoppingCartVo> getCartItems(@Param("userId") Integer userId);

    /**
     * 清空指定用户的购物车
     * @param userId 用户ID
     * @return 影响的行数
     */
    int clearCart(@Param("userId") Integer userId);

    /**
     * 从购物车中删除指定商品
     * @param userId 用户ID
     * @param productId 商品ID
     * @return 影响的行数
     */
    int deleteCartItem(
            @Param("userId") Integer userId,
            @Param("productId") Integer productId
    );

    /**
     * 添加商品到购物车或更新已有商品数量
     * @param userId 用户ID
     * @param productId 商品ID
     * @param quantity 商品数量
     * @return 影响的行数
     */
    int saveOrUpdateCartItem(
            @Param("userId") Integer userId,
            @Param("productId") Integer productId,
            @Param("quantity") Integer quantity
    );
}
