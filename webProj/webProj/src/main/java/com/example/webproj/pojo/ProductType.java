package com.example.webproj.pojo;

import java.time.LocalDateTime;

public class ProductType {
    private Integer id;
    private Integer parentId;
    private String name;
    private Integer sortOrder;
    private Boolean status;  // 对应tinyint(1)
    private Integer level;
    private LocalDateTime created;
    private LocalDateTime updated;

    // 无参构造函数
    public ProductType() {
    }

    // 全参构造函数
    public ProductType(Integer id, Integer parentId, String name, Integer sortOrder,
                       Boolean status, Integer level, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.sortOrder = sortOrder;
        this.status = status;
        this.level = level;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    // 业务方法 - 判断是否为顶级分类
    public boolean isRootCategory() {
        return parentId == null || parentId == 0;
    }

    // 业务方法 - 判断分类是否可用
    public boolean isActive() {
        return status != null && status;
    }

    @Override
    public String toString() {
        return "ProductType{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", sortOrder=" + sortOrder +
                ", status=" + status +
                ", level=" + level +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}