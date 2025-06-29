package com.example.webproj.pojo;

import java.math.BigDecimal;

public class ShoppingCartVo {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private Integer status;
    private BigDecimal totalPrice;
    private Integer stock;
    private String iconUrl;
    private Integer checked;

    public ShoppingCartVo(Integer id, Integer userId, Integer productId, String name, Integer quantity, BigDecimal price, Integer status, BigDecimal totalPrice, Integer stock, String iconUrl, Integer checked) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.totalPrice = totalPrice;
        this.stock = stock;
        this.iconUrl = iconUrl;
        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getStock() {
        return stock;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public BigDecimal getTotalPrice() {
        if (this.price == null || this.quantity == null) {
            return BigDecimal.ZERO;
        }
        return this.price.multiply(new BigDecimal(this.quantity));
    }
}
