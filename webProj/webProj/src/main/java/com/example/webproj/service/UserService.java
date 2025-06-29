package com.example.webproj.service;

import com.example.webproj.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserService {
    boolean register(User user);
    User do_login(String account, String password);
    User login(String account, String password);
    User getUserinfo(String account);
    User getuserquestion(String account);
    User getUserByAccount(String account);
    User findUserById(Integer id);
    List<User> findAllUsers();
    Map<String, Object> checkInfo(String info, String type);
    boolean deleteUser(Integer id);
    boolean updateUser(User user);
    boolean updateUserinfo(User user);
    boolean checkValid(String account);
    boolean checkUserAnswer(String account, String question, String asw);
    boolean updatePassword(String account, String oldpassword, String newpassword);


    boolean checkSecurityAnswer(String account, String asw);
    int updateUserPassword(String account, String newpwd);
}
