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
                                 @Param("size")   int size);

    Order getDetailByOrderNo(@Param("orderNo") Long orderNo);

    /* ============ 用户端 ============ */
    List<Order> findUserOrders(@Param("uid")     Integer uid,
                               @Param("status")  Integer status,
                               @Param("offset")  int     offset,
                               @Param("size")    int     size);

    /* ============ 状态更新 ============ */
    int updateOrderStatus(@Param("orderNo")    Long          orderNo,
                          @Param("status")     Integer       status,
                          @Param("updateTime") LocalDateTime updateTime);

    int insertOrder(Order order);        // 创建订单
}