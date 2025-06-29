package com.example.webproj.service;

import com.example.webproj.mappers.OrderMapper;
import com.example.webproj.mappers.ShoppingCartMapper;
import com.example.webproj.pojo.Address;
import com.example.webproj.pojo.Order;
import com.example.webproj.pojo.OrderItem;
import com.example.webproj.pojo.ShoppingCartVo;
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
    private final ShoppingCartMapper shoppingCartMapper;

    public OrderServiceImpl(OrderMapper orderMapper, ShoppingCartMapper shoppingCartMapper) {
        this.orderMapper = orderMapper;
        this.shoppingCartMapper = shoppingCartMapper;
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
    public Order createOrder(Integer uid, Integer addrId) {
        // 1) 获取用户购物车中的商品
        List<ShoppingCartVo> cartItems = shoppingCartMapper.getCartItems(uid);

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空，无法创建订单");
        }

        // 2) 校验库存
        for (ShoppingCartVo item : cartItems) {
            if (item.getStock() < item.getQuantity()) {
                throw new RuntimeException("商品 " + item.getName() + " 库存不足");
            }
        }

        // 3) 计算订单总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (ShoppingCartVo item : cartItems) {
            totalAmount = totalAmount.add(item.getTotalPrice());
        }

        // 4) 生成订单主表
        Order order = new Order();
        order.setOrderNo(System.currentTimeMillis());  // 使用时间戳作为订单号
        order.setUid(uid);
        order.setAddrId(addrId);
        order.setAmount(totalAmount);
        order.setType(1);  // 在线支付
        order.setFreight(0);  // 免运费
        order.setStatus(1); // 未付款
        order.initCreateTime();

        // 5) 插入订单主表
        int orderResult = orderMapper.insertOrder(order);
        if (orderResult <= 0) {
            throw new RuntimeException("创建订单失败");
        }

        // 6) 批量插入订单项
        for (ShoppingCartVo cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setUid(uid);
            orderItem.setOrderId(order.getId());
            orderItem.setGoodsId(cartItem.getProductId());
            orderItem.setGoodsName(cartItem.getName());
            orderItem.setIconUrl(cartItem.getIconUrl());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            orderItem.initCreateTime();

            int itemResult = orderMapper.insertOrderItem(orderItem);
            if (itemResult <= 0) {
                throw new RuntimeException("创建订单项失败");
            }
        }

        // 7) 扣减商品库存
        for (ShoppingCartVo cartItem : cartItems) {
            int stockResult = orderMapper.decreaseProductStock(cartItem.getProductId(), cartItem.getQuantity());
            if (stockResult <= 0) {
                throw new RuntimeException("扣减库存失败，商品 " + cartItem.getName() + " 库存不足");
            }
        }

        // 8) 清空用户购物车
        int clearResult = orderMapper.clearUserCart(uid);
        if (clearResult < 0) {
            throw new RuntimeException("清空购物车失败");
        }

        // 9) 返回完整的订单信息
        return getDetail(order.getOrderNo());
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