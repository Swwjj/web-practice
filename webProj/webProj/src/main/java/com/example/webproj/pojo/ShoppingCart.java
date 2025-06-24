package com.example.webproj.pojo;

import java.time.LocalDateTime;

public class ShoppingCart {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer quantity;
    private LocalDateTime created;
    private LocalDateTime updated;

    // 无参构造函数
    public ShoppingCart() {
    }

    // 全参构造函数
    public ShoppingCart(Integer id, Integer userId, Integer productId,
                        Integer quantity, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.created = created;
        this.updated = updated;
    }

    // Getter和Setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

    // 自动设置时间的方法
    public void initCreateTime() {
        this.created = LocalDateTime.now();
        this.updated = LocalDateTime.now();
    }

    public void updateTime() {
        this.updated = LocalDateTime.now();
    }

    // 业务方法 - 增加商品数量
    public void increaseQuantity(int amount) {
        if (amount > 0) {
            this.quantity += amount;
            updateTime();
        }
    }

    // 业务方法 - 减少商品数量
    public void decreaseQuantity(int amount) {
        if (amount > 0 && this.quantity >= amount) {
            this.quantity -= amount;
            updateTime();
        }
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}