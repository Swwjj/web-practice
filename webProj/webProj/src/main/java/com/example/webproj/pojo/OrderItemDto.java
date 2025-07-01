package com.example.webproj.pojo;

public class OrderItemDto {
    private Integer productId;
    private Integer quantity;
    private Double price;

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public void setPrice(Double price) { this.price = price; }

    public double getPrice() {
        return price;
    }
}