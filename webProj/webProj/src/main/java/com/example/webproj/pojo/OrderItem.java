package com.example.webproj.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderItem {
    private Integer id;
    private Integer uid;
    private Integer orderId;
    private Integer goodsId;
    private String goodsName;
    private String iconUrl;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime created;
    private LocalDateTime updated;

    // 无参构造函数
    public OrderItem() {
    }

    // 全参构造函数（可选）
    public OrderItem(Integer id, Integer uid, Integer orderId, Integer goodsId,
                     String goodsName, String iconUrl, BigDecimal price,
                     Integer quantity, BigDecimal totalPrice,
                     LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.uid = uid;
        this.orderId = orderId;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.iconUrl = iconUrl;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.created = created;
        this.updated = updated;
    }

    // Getter 和 Setter 方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    // 自动设置创建和更新时间的方法（需要手动调用）
    public void initCreateTime() {
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    public void updateTime() {
        this.updated = LocalDateTime.now();
    }

    // 计算总价的方法
    public void calculateTotalPrice() {
        if (this.price != null && this.quantity != null) {
            this.totalPrice = this.price.multiply(BigDecimal.valueOf(this.quantity));
        }
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", uid=" + uid +
                ", orderId=" + orderId +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}