package com.example.webproj.service;

import com.example.webproj.mappers.OrderMapper;
import com.example.webproj.mappers.ProductMapper;
import com.example.webproj.mappers.ShoppingCartMapper;
import com.example.webproj.pojo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ProductMapper productMapper;

    public OrderServiceImpl(OrderMapper orderMapper, ShoppingCartMapper shoppingCartMapper, ProductMapper productMapper) {
        this.orderMapper = orderMapper;
        this.shoppingCartMapper = shoppingCartMapper;
        this.productMapper = productMapper;
    }

    /* ========== 管理端 ========== */

    @Override
    public List<Order> findOrdersNoPages(Long orderNo) {
        List<Order> orders = orderMapper.findOrdersNoPages(orderNo);
        // 为每个订单设置描述字段
        for (Order order : orders) {
            setOrderDescriptions(order);
        }
        return orders;
    }

    @Override
    public PageResult<Order> findOrdersPaging(int pageNum, int pageSize) {
        int orderNum = orderMapper.getOrderCount();
        int offset = (pageNum - 1) * pageSize;
        List<Order> list = orderMapper.findOrdersPaging(offset, pageSize);
        // 为每个订单设置描述字段
        for (Order order : list) {
            setOrderDescriptions(order);
        }
        return new PageResult<>(pageNum, pageSize, orderNum, list);
    }

    @Override
    public List<Order> searchOrders(Long orderNo, int pageNum, int pageSize) {
        List<Order> orders = orderMapper.findOrdersNoPages(orderNo);
        // 为每个订单设置描述字段
        for (Order order : orders) {
            setOrderDescriptions(order);
        }
        return orders;
    }

    @Override
    public Order getDetail(Long orderNo) {
        Order order = orderMapper.getDetailByOrderNo(orderNo);
        if (order != null) {
            setOrderDescriptions(order);
        }
        return order;
    }

    /* ========== 用户端 ========== */

    @Override
    public PageResult<Order> getUserOrders(Integer uid, Integer status,
                                           int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        int orderNum = orderMapper.getOrderCountByUserId(uid);
        List<Order> list = orderMapper.findUserOrders(uid, status, offset, pageSize);
        // 为每个订单设置描述字段
        for (Order order : list) {
            setOrderDescriptions(order);
        }
        return new PageResult<>(pageNum, pageSize, orderNum, list);
    }

    @Override
    @Transactional
    public Order createOrder(Integer uid, Integer addrId, List<OrderItemDto> items) {
        // 1. 创建订单对象
        Order order = new Order();
        // 生成唯一订单号（用当前时间戳）
        order.setOrderNo(System.currentTimeMillis());
        order.setUserId(uid);
        order.setAddrId(addrId);
        double amount = 0;
        for (OrderItemDto dto : items) {
           amount += dto.getPrice() * dto.getQuantity();
        }
        order.setAmount(new java.math.BigDecimal(amount));
        order.setType(1);
        order.setFreight(0);
        order.setStatus(1);
        order.setCreated(LocalDateTime.now());
        // TODO: 设置订单其他属性

        // 2. 遍历 items，生成订单项
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto dto : items) {
            OrderItem item = new OrderItem();
            item.setUid(uid);
            item.setGoodsId(dto.getProductId());
            item.setQuantity(dto.getQuantity());
            double price = dto.getPrice();
            item.setTotalPrice(BigDecimal.valueOf(price));

            Integer id = dto.getProductId();
            Product product = productMapper.getProductByMainId(id);

            item.setGoodsName(product.getName());
            item.setPrice(product.getPrice());
            item.setCreated(LocalDateTime.now());
            item.setIconUrl(String.valueOf(111));
            // TODO: 设置商品价格、名称等其他属性

            orderItems.add(item);
        }
         //TODO: 保存订单和订单项到数据库
         orderMapper.insertOrder(order);
         for (OrderItem item : orderItems) {
             item.setOrderNo(order.getOrderId());
             orderMapper.insertOrderItem(item);
         }
        shoppingCartMapper.clearCart(order.getOrderId());
        // 这里只返回订单对象，实际应返回包含订单项的完整订单
        return order;
    }

    @Override
    public Boolean confirmReceipt(Integer uid, Long orderNo) {
        // 获取订单详情
        Order order = getDetail(orderNo);
        if (order == null || !order.getUid().equals(uid)) {
            return false; // 订单不存在或不属于当前用户
        }
        
        // 检查订单状态是否为已发货
        if (order.getStatus() != 3) {
            return false; // 订单状态不是已发货，不能确认收货
        }
        
        // 更新订单状态为已完成
        orderMapper.updateOrderStatus(uid, orderNo, 4);
        return true;
    }

    @Override
    public void cancelOrder(Integer uid, Long orderNo) {
        // 校验状态，更新为取消
        orderMapper.updateOrderStatus(uid, orderNo, 6);
    }

    /**
     * 设置订单的描述字段
     * @param order 订单对象
     */
    private void setOrderDescriptions(Order order) {
        // 设置支付类型描述
        if (order.getType() != null) {
            switch (order.getType()) {
                case 1:
                    order.setTypeDesc("在线支付");
                    break;
                case 2:
                    order.setTypeDesc("货到付款");
                    break;
                default:
                    order.setTypeDesc("未知支付方式");
                    break;
            }
        }

        // 设置订单状态描述
        if (order.getStatus() != null) {
            switch (order.getStatus()) {
                case 1:
                    order.setStatusDesc("未付款");
                    break;
                case 2:
                    order.setStatusDesc("已付款");
                    break;
                case 3:
                    order.setStatusDesc("已发货");
                    break;
                case 4:
                    order.setStatusDesc("交易成功");
                    break;
                case 5:
                    order.setStatusDesc("交易关闭");
                    break;
                case 6:
                    order.setStatusDesc("订单取消");
                    break;
                default:
                    order.setStatusDesc("未知状态");
                    break;
            }
        }
    }
}