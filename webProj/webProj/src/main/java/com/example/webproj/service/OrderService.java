package com.example.webproj.service;

import com.example.webproj.pojo.Order;
import com.example.webproj.pojo.PageResult;

import java.util.List;
import java.util.Map;

public interface OrderService {
    /* ===== 管理端 ===== */
    List<Order> findOrdersNoPages(Long orderNo);

    PageResult<Order> findOrdersPaging(int pageNum, int pageSize);

    List<Order> searchOrders(Long orderNo, int pageNum, int pageSize);

    Order getDetail(Long orderNo);

    /* ===== 用户端 ===== */
    PageResult<Order> getUserOrders(Integer uid,
                                    Integer status,
                                    int pageNum,
                                    int pageSize);

    Order createOrder(Integer uid, Integer addrId);

    void confirmReceipt(Long orderNo);

    void cancelOrder(Integer uid, Long orderNo);
}
