package com.example.webproj.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private Integer id;
    private String name;
    private Integer productId;
    private Integer partsId;
    private String iconUrl;
    private String subImages;
    private String detail;
    private String specParam;
    private BigDecimal price;
    private Integer stock;
    private Integer status = 1;       // 默认值1
    private Integer isHot = 2;        // 默认值2
    private LocalDateTime created;
    private LocalDateTime updated;
    private Integer categoryId;

    // 无参构造函数
    public Product() {
    }

    // 全参构造函数
    public Product(Integer id, String name, Integer productId, Integer partsId,
                   String iconUrl, String subImages, String detail, String specParam,
                   BigDecimal price, Integer stock, Integer status, Integer isHot,
                   LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.name = name;
        this.productId = productId;
        this.partsId = partsId;
        this.iconUrl = iconUrl;
        this.subImages = subImages;
        this.detail = detail;
        this.specParam = specParam;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.isHot = isHot;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getPartsId() {
        return partsId;
    }

    public void setPartsId(Integer partsId) {
        this.partsId = partsId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getSubImages() {
        return subImages;
    }

    public void setSubImages(String subImages) {
        this.subImages = subImages;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSpecParam() {
        return specParam;
    }

    public void setSpecParam(String specParam) {
        this.specParam = specParam;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
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

    // 业务方法 - 检查库存是否充足
    public boolean isStockEnough(int requiredQuantity) {
        return this.stock != null && this.stock >= requiredQuantity;
    }

    // 业务方法 - 减少库存
    public void reduceStock(int quantity) {
        if (this.stock != null && this.stock >= quantity) {
            this.stock -= quantity;
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productId=" + productId +
                ", partsId=" + partsId +
                ", iconUrl='" + iconUrl + '\'' +
                ", subImages='" + subImages + '\'' +
                ", detail='" + detail + '\'' +
                ", specParam='" + specParam + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", status=" + status +
                ", isHot=" + isHot +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}