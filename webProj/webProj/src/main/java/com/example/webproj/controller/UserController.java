package com.example.webproj.controller;

import com.example.webproj.mappers.UserMapper;
import com.example.webproj.pojo.User;
import com.example.webproj.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/actionmall/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

//    @PostMapping("/do_register.do")
//    public Map<String, Object> register(@RequestBody User user) {
//        boolean result = userService.register(user);
//        return result ? success("注册成功！") : error("注册失败！");
//    }

    @PostMapping("/do_register.do")
    public Map<String, Object> register(@RequestBody User user) {
        // 先检查用户名
        if (userMapper.findByAccount(user.getAccount()) != null) {
            return error("用户名已经存在");
        }

        // 检查邮箱
        if (user.getEmail() != null && !user.getEmail().isEmpty() &&
                userMapper.findByEmail(user.getEmail()) != null) {
            return error("Email已经存在");
        }

        // 检查电话
        if (user.getPhone() != null && !user.getPhone().isEmpty() &&
                userMapper.findByPhone(user.getPhone()) != null) {
            return error("电话号码已经存在！");
        }

        // 执行注册
        boolean result = userService.register(user);
        return result ? success("注册成功！") : error("注册失败！");
    }

    @PostMapping("/do_check_info.do")
    public Map<String, Object> checkInfo(
            @RequestParam String info,
            @RequestParam String type) {

        return userService.checkInfo(info, type);
    }

    @PostMapping("/do_login.do")
    public Map<String, Object> do_login(@RequestParam String account, @RequestParam String password, HttpSession session) {
        User user = userService.do_login(account, password);
        if (user == null) {
            return error("账号或密码错误");
        }

        if (user.getRole() != null && user.getRole() != 1) {
            return error("身份错误");
        }

        session.setAttribute("user", user);
        return success("用户登录成功", user);
    }


    @PostMapping("/login.do")
    public Map<String, Object> adminLogin(@RequestParam String account, @RequestParam String password, HttpSession session) {
        User user = userService.login(account, password);

        if (user == null) {
            return error("账号或密码错误");
        }

        if (!user.isAdmin()) {
            return error("该账号不是管理员");
        }

        session.setAttribute("user", user); // 存入 session
        user.setPassword(generateUUID());
        return success("管理员登录成功", user);
    }

    @PostMapping("/deleteusers.do")
    public Map<String, Object> deleteUser(@RequestParam String id) {
        try {
            Integer userId = Integer.parseInt(id);
            boolean result = userService.deleteUser(userId);

            if (result) {
                return success("删除成功！");
            } else {
                return error("用户存在订单无法删除！");
            }
        } catch (NumberFormatException e) {
            return error("无效的用户ID");
        }
    }

    @PostMapping("/finduser.do")
    public Map<String, Object> findUserById(@RequestParam Integer id) {
        User user = userService.findUserById(id);

        if (user != null) {
            return success("查询成功！", user);
        } else {
            return error("获取用户数据失败");
        }
    }

    @PostMapping("/finduserlist.do")
    public Map<String, Object> findAllUsers() {
        List<User> userList = userService.findAllUsers();
        if (userList != null && !userList.isEmpty()) {
            return success("查询成功", userList);
        } else {
            return error("暂无用户数据");
        }
    }

    @PostMapping("/getUserByAccount.do")
    public ResponseEntity<?> getUserByAccount(@RequestParam String account) {
        User user = userService.getUserByAccount(account);
        if (user != null) {
            return ResponseEntity.ok(user); // 成功时直接返回用户对象
        }
        // 失败时返回结构化错误信息
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", 1);
        errorResponse.put("msg", "用户名错误！");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @PostMapping("/getuserquestion.do")
    public Map<String, Object> getuserquestion(@RequestParam String account) {
        User user = userService.getuserquestion(account);
        if (user != null && user.getQuestion() != null) {
            return success(user.getQuestion());
        }
        return error("未设置密码提示问题");
    }

    @PostMapping("/checkuserasw.do")
    public Map<String, Object> checkUserAnswer(@RequestParam String account, @RequestParam String question, @RequestParam String asw) {
        boolean isValid = userService.checkUserAnswer(account, question, asw);
        if (isValid) {
            return success("验证通过", generateUUID());
        } else {
            return error("问题答案错误！");
        }
    }

    @PostMapping("/resetpassword.do")
    public Map<String, Object> resetPassword(@RequestParam int id, @RequestParam String asw, @RequestParam String newpwd) {

        boolean isValid = userService.checkSecurityAnswer(id, asw);
        if (!isValid) {
            return error("密保答案错误！");
        }

        int rowsAffected = userService.updateUserPassword(id, newpwd);
        if (rowsAffected > 0) {
            return success("密码修改成功！");
        } else {
            return error("密码修改失败！");
        }
    }

    private String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    @PostMapping("/updateuser.do")
    public Map<String, Object> updateUser(@RequestBody User user) {
        boolean result = userService.updateUser(user);

        if (result) {
            User user1 = userService.getUserinfo(user.getAccount());
            user1.setPassword(generateUUID());
            return success("用户信息修改成功！", user1);
        } else {
            return error("用户信息修改失败！");
        }
    }

    @PostMapping("/updateuserinfo.do")
    public Map<String, Object> updateUserInfo(@RequestBody Map<String, String> body, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return error("用户未登录");
        }
        try {
            user.setEmail(body.get("email"));
            user.setPhone(body.get("phone"));
            user.setQuestion(body.get("question"));
            user.setAsw(body.get("asw"));
            user.setAge(Integer.parseInt(body.get("age")));

            String sexStr = body.get("sex");
            if ("男".equals(sexStr)) {
                user.setSex(true);
            } else if ("女".equals(sexStr)) {
                user.setSex(false);
            } else {
                return error("性别格式错误，应为“男”或“女”");
            }

            user.updateTime(); // 设置更新时间

            boolean result = userService.updateUserinfo(user);

            if (result) {
                user.setPassword(generateUUID());
                return success("用户信息更新成功", user);
            } else {
                return error("更新失败，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return error("参数异常：" + e.getMessage());
        }
    }


    @PostMapping("/updatepassword.do")
    public Map<String, Object> updatePassword(@RequestParam String newpwd, @RequestParam String oldpwd, HttpSession session) {
        // 获取当前登录用户
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return error("请先登录！");
        }

        // 调用服务层方法进行密码更新
        boolean result = userService.updatePassword(currentUser.getAccount(), oldpwd, newpwd);
        if (result) {
            return success("密码修改成功！");
        } else {
            return error("原始密码错误！");
        }
    }



//    @PostMapping("/getuserinfo.do")
//    public ResponseEntity<?> getUserinfo(@RequestParam String account) {
//        User user = userService.getUserinfo(account);
//        if (user != null) {
//            return ResponseEntity.ok(user); // 成功时直接返回用户对象
//        }
//
//        // 失败时返回结构化错误信息
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("status", 1);
//        errorResponse.put("msg", "无法获取用户信息！");
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//    }

    @PostMapping("/getuserinfo.do")
    public ResponseEntity<?> getUserinfo(HttpSession session) {
       User user = (User) session.getAttribute("user");
        if (user == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", 1);
            errorResponse.put("msg", "用户未登录或会话已过期！");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        String account = user.getAccount();
        User user1 = userService.getUserinfo(account);

        if (user1 != null) {
            return ResponseEntity.ok(user1); // 成功返回用户对象
        }

        // 用户不存在或其他错误
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", 1);
        errorResponse.put("msg", "无法获取用户信息！");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @GetMapping("/check_valid.do")
    public Map<String, Object> checkValid(@RequestParam String account) {
        boolean valid = userService.checkValid(account);
        return valid ? success("可以使用") : error("账号已存在");
    }

    @GetMapping("/get_user_info.do")
    public Map<String, Object> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return error("用户未登录");
        }
        return success("查询成功", user);
    }

    @PostMapping("/do_logout.do")
    public Map<String, Object> logout(HttpSession session) {
        // 清除会话中的用户信息
        session.removeAttribute("user");
        // 使会话失效（可选）
        session.invalidate();

        return success("登出成功！");
    }

    // 通用返回结构
    private Map<String, Object> success(String msg) {
        return Map.of("status", 0, "msg", msg);
    }

    private Map<String, Object> success(String msg, Object data) {
        return Map.of("status", 0, "msg", msg, "data", data);
    }

    private Map<String, Object> error(String msg) {
        return Map.of("status", 1, "msg", msg);
    }
}
