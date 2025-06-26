package com.example.webproj.service;

import com.example.webproj.mappers.OrderMapper;
import com.example.webproj.pojo.Address;
import com.example.webproj.pojo.Order;
import com.example.webproj.pojo.OrderItem;
import org.springframework.stereotype.Service;

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


}
