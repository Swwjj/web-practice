package com.example.webproj.pojo;
import java.time.LocalDateTime;

public class Address {
    private Integer id;
    private Integer userId;
    private String name;
    private String phone;
    private String mobile;
    private String province;
    private String city;
    private String district;
    private String addr;
    private String zip;
    private Boolean dfault = false;  // 默认值false
    private Boolean isDel = false;   // 默认值false
    private LocalDateTime created;
    private LocalDateTime updated;

    // 无参构造函数
    public Address() {
    }

    // 全参构造函数
    public Address(Integer id, Integer userId, String name, String phone,
                   String mobile, String province, String city, String district,
                   String addr, String zip, Boolean dfault, Boolean isDel,
                   LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.mobile = mobile;
        this.province = province;
        this.city = city;
        this.district = district;
        this.addr = addr;
        this.zip = zip;
        this.dfault = dfault;
        this.isDel = isDel;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Boolean getDfault() {
        return dfault;
    }

    public void setDfault(Boolean dfault) {
        this.dfault = dfault;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
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

    // 业务方法 - 获取完整地址
    public String getFullAddress() {
        return String.format("%s%s%s%s",
                province != null ? province : "",
                city != null ? city : "",
                district != null ? district : "",
                addr != null ? addr : "");
    }

    // 业务方法 - 判断是否为有效地址
    public boolean isValid() {
        return !isDel && addr != null && !addr.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", mobile='" + mobile + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", addr='" + addr + '\'' +
                ", zip='" + zip + '\'' +
                ", dfault=" + dfault +
                ", isDel=" + isDel +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}