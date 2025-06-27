package com.example.webproj.service;

import com.example.webproj.mappers.UserMapper;
import com.example.webproj.pojo.Order;
import com.example.webproj.pojo.User;
import com.example.webproj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    // 在UserServiceImpl中修改register方法
    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (userMapper.findByAccount(user.getAccount()) != null) {
            return false;
        }

        // 检查邮箱是否已存在
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (userMapper.findByEmail(user.getEmail()) != null) {
                return false;
            }
        }

        // 检查电话是否已存在
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            if (userMapper.findByPhone(user.getPhone()) != null) {
                return false;
            }
        }

        // 设置默认值
        user.setRole(1); // 普通用户
        user.setAge(0);
        user.initCreateTime();
        user.setSex(true);
        user.setDel(false);
        user.setName("用户");

        // 插入用户
        return userMapper.insertUser(user) > 0;
    }

    @Override
    public Map<String, Object> checkInfo(String info, String type) {
        Map<String, Object> result = new HashMap<>();

        switch (type) {
            case "account":
                if (userMapper.findByAccount(info) != null) {
                    result.put("status", 1);
                    result.put("msg", "用户名已经存在");
                } else {
                    result.put("status", 0);
                    result.put("msg", "信息验证成功！");
                }
                break;

            case "email":
                if (userMapper.findByEmail(info) != null) {
                    result.put("status", 1);
                    result.put("msg", "Email已经存在");
                } else {
                    result.put("status", 0);
                    result.put("msg", "信息验证成功！");
                }
                break;

            case "phone":
                if (userMapper.findByPhone(info) != null) {
                    result.put("status", 1);
                    result.put("msg", "电话号码已经存在！");
                } else {
                    result.put("status", 0);
                    result.put("msg", "信息验证成功！");
                }
                break;

            default:
                result.put("status", 1);
                result.put("msg", "信息验证类别错误！");
        }

        return result;
    }


    @Override
    public User do_login(String account, String password) {
        User user = userMapper.findByAccount(account);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public User login(String account, String password) {
        User user = userMapper.findByAccount(account);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        // 检查用户是否存在订单
        int orderCount = userMapper.checkUserHasOrders(userId);
        if (orderCount > 0) {
            return false; // 用户存在订单，不能删除
        }

        // 删除用户
        int rowsAffected = userMapper.deleteUserById(userId);
        return rowsAffected > 0;
    }

    public User findUserById(Integer id) {
        return userMapper.findById(id);
    }

    @Override
    public List<User> findAllUsers() {
        return userMapper.findAll(); // 假设你使用了 JpaRepository
    }

    @Override
    public boolean updateUser(User user) {
        // 可以先检查用户是否存在
        User existingUser = userMapper.findByAccount(user.getAccount());
        if (existingUser == null || existingUser.getId() != user.getId()) {
            return false; // 用户不存在或账号不匹配
        }
        int result = userMapper.updateUser(user);
        return result > 0;
    }

    @Override
    public boolean updateUserinfo(User user) {
        // 可以先检查用户是否存在
        int updated = userMapper.updateUserinfo(user);
        return updated > 0;
    }

    public User getuserquestion(String account) {
        User user = userMapper.finduserquestion(account);
        if(user != null) {
            return user;
        }
        return null;
    }

    public boolean checkUserAnswer(String account, String question, String asw) {
        User user = userMapper.findByAccount(account);
        return user != null && user.getQuestion().equals(question) && user.getAsw().equals(asw);
    }

    @Override
    public boolean checkSecurityAnswer(int id, String asw) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return false;
        }
        return asw.equals(user.getAsw()); // 假设 User 类中用 getAsw() 获取密保答案
    }

    @Override
    public int updateUserPassword(int id, String newpwd) {
        return userMapper.updatePasswordById(id, newpwd);
    }

    public User getUserByAccount(String account) {
        return userMapper.findByAccountnoasw(account);
    }


    public boolean updatePassword(String account, String oldpwd, String newpwd) {
        // 查找用户
        User user = userMapper.findByAccount(account);
        if (user == null || !user.getPassword().equals(oldpwd)) {
            return false; // 用户不存在或旧密码不匹配
        }

        // 更新密码
        int rowsAffected = userMapper.updatePassword(account, newpwd);
        return rowsAffected > 0;
    }


    public User getUserinfo(String account) {
        User user = userMapper.getUserinfo(account);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public boolean checkValid(String account) {
        return userMapper.findByAccount(account) == null;
    }
}