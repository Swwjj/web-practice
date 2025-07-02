package com.example.webproj.mappers;

import com.example.webproj.pojo.Address;
import com.example.webproj.pojo.Order;
import com.example.webproj.pojo.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    int getOrderCount();
    /* ============ 管理端 ============ */
    List<Order> findOrdersNoPages(@Param("orderNo") Long orderNo);

    List<Order> findOrdersPaging(@Param("offset") int offset,
                                 @Param("pageSize")   int pageSize);

    Order getDetailByOrderNo(@Param("orderNo") Long orderNo);

    /* ============ 用户端 ============ */
    List<Order> findUserOrders(@Param("uid")     Integer uid,
                               @Param("status")  Integer status,
                               @Param("offset")  int     offset,
                               @Param("pageSize")    int     pageSize);

    /* ============ 状态更新 ============ */
    int updateOrderStatus(@Param("userId") Integer uid,
                          @Param("orderNo")    Long          orderNo,
                          @Param("status")    int          status);


    int getOrderCountByUserId(@Param("uid")     Integer uid);

    /* ============ 创建订单相关 ============ */



    int decreaseProductStock(@Param("productId") Integer productId, 
                            @Param("quantity") Integer quantity);  // 扣减商品库存

    void insertOrder(Order order);

    void insertOrderItem(OrderItem item);
}