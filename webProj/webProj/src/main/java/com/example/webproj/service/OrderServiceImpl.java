package com.example.webproj.service;

import com.example.webproj.mappers.OrderMapper;
import com.example.webproj.pojo.Address;
import com.example.webproj.pojo.Order;
import com.example.webproj.pojo.OrderItem;
import com.example.webproj.pojo.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /* ========== 管理端 ========== */

    @Override
    public List<Order> findOrdersNoPages(Long orderNo) {
        return orderMapper.findOrdersNoPages(orderNo);
    }

    @Override
    public PageResult<Order> findOrdersPaging(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Order> list = orderMapper.findOrdersPaging(offset, pageSize);
        // 真实项目中需要再查 count(*)，此处示例直接给 list.size()
        return new PageResult<>(pageNum, pageSize, list.size(), list);
    }

    @Override
    public PageResult<Order> searchOrders(Long orderNo, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Order> list = orderMapper.searchOrders(orderNo, offset, pageSize);
        return new PageResult<>(pageNum, pageSize, list.size(), list);
    }

    @Override
    public Order getDetail(Long orderNo) {
        return orderMapper.getDetailByOrderNo(orderNo);
    }

    /* ========== 用户端 ========== */

    @Override
    public PageResult<Order> getUserOrders(Integer uid, Integer status,
                                           int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<Order> list = orderMapper.findUserOrders(uid, status, offset, pageSize);
        return new PageResult<>(pageNum, pageSize, list.size(), list);
    }

    @Override
    @Transactional
    public Order createOrder(Integer uid, Integer addrId) {
        // 1) 校验购物车、库存等（略）
        // 2) 生成订单主表
        Order order = new Order();
        order.setOrderNo(System.currentTimeMillis());  // 简示例
        order.setUid(uid);
        order.setAddrId(addrId);
        //order.setAmount(/* 计算金额 */);
        order.setType(1);
        order.setFreight(0);
        order.setStatus(1); // 未付款
        order.initCreateTime();
        orderMapper.insertOrder(order);

        // 3) 批量插入 order_item（自行调用 ItemMapper）
        // 4) 清空购物车、扣减库存（略）

        return getDetail(order.getOrderNo());
    }

    @Override
    public void confirmReceipt(Integer uid, Long orderNo) {
        // 仅允许本人且订单已发货
        orderMapper.updateOrderStatus(orderNo, 5, LocalDateTime.now());
    }

    @Override
    public void cancelOrder(Integer uid, Long orderNo) {
        // 校验状态，更新为取消
        orderMapper.updateOrderStatus(orderNo, 6, LocalDateTime.now());
    }
}