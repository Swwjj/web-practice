package com.example.webproj.pojo;

import java.util.List;

public class OrderCreateRequest {
    private Integer addrId;
    private List<OrderItemDto> items;

    public Integer getAddrId() { return addrId; }
    public void setAddrId(Integer addrId) { this.addrId = addrId; }
    public List<OrderItemDto> getItems() { return items; }
    public void setItems(List<OrderItemDto> items) { this.items = items; }
} 