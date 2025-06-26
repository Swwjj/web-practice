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

    public BigDecimal getTotalPrice() {
        if (this.price == null || this.quantity == null) {
            return BigDecimal.ZERO;
        }
        return this.price.multiply(new BigDecimal(this.quantity));
    }
}
