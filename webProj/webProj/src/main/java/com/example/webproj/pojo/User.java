package com.example.webproj.pojo;

import java.time.LocalDateTime;

public class User {
    private Integer id;
    private String account;
    private String password;
    private String email;
    private String phone;
    private String question;
    private String asw;
    private Integer role;
    private Integer age;
    private Boolean sex; // 对应tinyint(1)
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Boolean del = false; // 默认值false
    private String name;

    // 无参构造函数
    public User() {
    }

    // 全参构造函数
    public User(Integer id, String account, String password, String email,
                String phone, String question, String asw, Integer role,
                Integer age, Boolean sex, LocalDateTime createTime,
                LocalDateTime updateTime, Boolean del, String name) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.question = question;
        this.asw = asw;
        this.role = role;
        this.age = age;
        this.sex = sex;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.del = del;
        this.name = name;
    }

    // Getter和Setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAsw() {
        return asw;
    }

    public void setAsw(String asw) {
        this.asw = asw;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDel() {
        return del;
    }

    public void setDel(Boolean del) {
        this.del = del;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 自动设置时间的方法
    public void initCreateTime() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    public void updateTime() {
        this.updateTime = LocalDateTime.now();
    }

    // 业务方法 - 判断是否为管理员
    public boolean isAdmin() {
        return role != null && role == 1; // 假设1表示管理员
    }

    // 业务方法 - 判断用户是否有效
    public boolean isActive() {
        return del != null && !del;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='[PROTECTED]'" +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", question='" + question + '\'' +
                ", asw='" + asw + '\'' +
                ", role=" + role +
                ", age=" + age +
                ", sex=" + sex +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", del=" + del +
                ", name='" + name + '\'' +
                '}';
    }
}