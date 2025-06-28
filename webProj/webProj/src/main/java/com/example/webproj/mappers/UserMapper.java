package com.example.webproj.mappers;

import com.example.webproj.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
        User findByAccount(String account);
        User selectById(int id);
        int updatePasswordById(int id, String newpwd);
        User findByAccountnoasw(String account);
        User getUserinfo(String account);
        User findByEmail(String email);
        User findByPhone(String phone);
        User finduserquestion(String account);
        User findById(int id);
        List<User> findAll();
        int insertUser(User user);
        int checkUserHasOrders(int id);
        int deleteUserById(int id);
        // 修改后的Mapper接口方法
        int updatePassword(
                @Param("account") String account,
                @Param("newpwd") String newpwd
        );
        int updateUser(User user);
        int updateUserinfo(User user);
}