package com.example.webproj.controller;
import com.example.webproj.pojo.Address;
import com.example.webproj.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/actionmall/addr")
public class AddressController {

    @Autowired
    private AddressService addressService;

    // 获取当前登录用户ID的方法（假设使用Session存储）
    // 这里需要实现获取当前登录用户ID的逻辑
    // 示例中返回固定值，实际中应从session或token中获取
    private Integer getCurrentUserId(HttpServletRequest request) {
        return request.getAttribute("userId") != null ?
                Integer.parseInt(request.getAttribute("userId").toString()) : null;
    }

    // 查询单个地址
    @PostMapping("/findAddressById.do")
    public Map<String, Object> findAddressById(@RequestBody Map<String, String> params,
                                               HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        //Integer uid = getCurrentUserId(request);  // Long → Integer

        Integer uid=1;
        if (uid == null) {
            result.put("status", 1);
            result.put("msg", "请登录后在进行操作！");
            return result;
        }

        String idStr = params.get("id");
        if (idStr == null || idStr.isEmpty()) {
            result.put("status", 1);
            result.put("msg", "参数错误！");
            return result;
        }

        try {
            Integer id = Integer.parseInt(idStr);  // Long → Integer
            Address address = addressService.findAddressById(id, uid);
            if (address != null) {
                result.put("status", 0);
                result.put("data", address);
            } else {
                result.put("status", 1);
                result.put("msg", "地址不存在！");
            }
        } catch (NumberFormatException e) {
            result.put("status", 1);
            result.put("msg", "参数错误！");
        }
        return result;
    }

    // 设置默认地址
    @GetMapping("/setdefault.do")
    public Map<String, Object> setDefaultAddress(@RequestParam("id") String idStr,
                                                 HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
//        Integer uid = getCurrentUserId(request);
          Integer uid = 1;
        if (uid == null) {
            result.put("status", 1);
            result.put("msg", "请登录后在进行操作！");
            return result;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            List<Address> addresses = addressService.setDefaultAddress(id, uid);
            result.put("status", 0);
            result.put("data", addresses);
        } catch (NumberFormatException e) {
            result.put("status", 1);
            result.put("msg", "默认地址设置失败！");
        }
        return result;
    }

    // 获取地址列表
    @GetMapping("/findaddrs.do")
    public Map<String, Object> findAddresses(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
//        Integer uid = getCurrentUserId(request);
        Integer uid = 1;
        if (uid == null) {
            result.put("status", 1);
            result.put("msg", "请登录后在进行操作！");
            return result;
        }

        List<Address> addresses = addressService.findAddressesByUid(uid);
        result.put("status", 0);
        result.put("data", addresses);
        return result;
    }

    // 删除地址
    @GetMapping("/deladdr.do")
    public Map<String, Object> deleteAddress(@RequestParam("id") String idStr,
                                             HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
//        Integer uid = getCurrentUserId(request);
        Integer uid = 2;
        if (uid == null) {
            result.put("status", 1);
            result.put("msg", "请登录后在进行操作！");
            return result;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            List<Address> addresses = addressService.deleteAddress(id, uid);
            result.put("status", 0);
            result.put("data", addresses);
        } catch (NumberFormatException e) {
            result.put("status", 1);
            result.put("msg", "地址删除失败！");
        }
        return result;
    }

    // 新增地址
    @PostMapping("/saveaddr.do")
    public Map<String, Object> saveAddress(@RequestBody Address address,
                                           HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
//        Integer uid = getCurrentUserId(request);
        Integer uid = 1;
        if (uid == null) {
            result.put("status", 1);
            result.put("msg", "请登录后在进行操作！");
            return result;
        }

        if (address.getName() == null || address.getMobile() == null ||
                address.getProvince() == null || address.getCity() == null ||
                address.getDistrict() == null || address.getAddr() == null ||
                address.getZip() == null) {
            result.put("status", 1);
            result.put("msg", "参数错误！");
            return result;
        }

        List<Address> addresses = addressService.saveAddress(address, uid);
        result.put("status", 0);
        result.put("data", addresses);
        return result;
    }
}

