package com.example.webproj.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private Integer id;
    private Long orderNo;
    private Integer aid;
    private Integer addrId;
    private BigDecimal amount;
    private Integer type;
    private Integer freight;
    private Integer status;
    private LocalDateTime paymentTime;
    private LocalDateTime deliveryTime;
    private LocalDateTime finishTime;
    private LocalDateTime closeTime;
    private LocalDateTime created;
    private LocalDateTime updated;

    // 无参构造函数
    public Order() {
    }

    // 全参构造函数（可选）
    public Order(Integer id, Long orderNo, Integer aid, Integer addrId, BigDecimal amount,
                 Integer type, Integer freight, Integer status, LocalDateTime paymentTime,
                 LocalDateTime deliveryTime, LocalDateTime finishTime, LocalDateTime closeTime,
                 LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.orderNo = orderNo;
        this.aid = aid;
        this.addrId = addrId;
        this.amount = amount;
        this.type = type;
        this.freight = freight;
        this.status = status;
        this.paymentTime = paymentTime;
        this.deliveryTime = deliveryTime;
        this.finishTime = finishTime;
        this.closeTime = closeTime;
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

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public Integer getAddrId() {
        return addrId;
    }

    public void setAddrId(Integer addrId) {
        this.addrId = addrId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getFreight() {
        return freight;
    }

    public void setFreight(Integer freight) {
        this.freight = freight;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderNo=" + orderNo +
                ", aid=" + aid +
                ", addrId=" + addrId +
                ", amount=" + amount +
                ", type=" + type +
                ", freight=" + freight +
                ", status=" + status +
                ", paymentTime=" + paymentTime +
                ", deliveryTime=" + deliveryTime +
                ", finishTime=" + finishTime +
                ", closeTime=" + closeTime +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}