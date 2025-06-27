package com.example.webproj.controller;

import com.example.webproj.pojo.Order;
import com.example.webproj.pojo.PageResult;
import com.example.webproj.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/actionmall")
public class OrderController {

    private final OrderService orderService;
    private final HttpSession  session;          // 简化示例，用 session 存 uid / admin

    public OrderController(OrderService orderService, HttpSession session) {
        this.orderService = orderService;
        this.session      = session;
    }

    /* =======================================================
     *                     管理端接口
     * ======================================================= */

    /** 管理端：订单列表（不分页） */
    @GetMapping("/mgr/order/findorders_nopages.do")
    public List<Order> mgrListNoPages(@RequestParam(required = false) Long orderNo) {
        // TODO: 判断是否为管理员
        return orderService.findOrdersNoPages(orderNo);
    }

    /** 管理端：订单列表（分页） */
    @PostMapping("/mgr/order/findorders.do")
    public PageResult<Order> mgrListPaging(@RequestParam(defaultValue = "1")  int pageNum,
                                           @RequestParam(defaultValue = "10") int pageSize) {
        // TODO: 判断是否为管理员
        return orderService.findOrdersPaging(pageNum, pageSize);
    }

    /** 管理端：根据订单号分页搜索 */
    @GetMapping("/mgr/order/search.do")
    public PageResult<Order> mgrSearch(@RequestParam Long orderNo,
                                       @RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        // TODO: 判断是否为管理员
        return orderService.searchOrders(orderNo, pageNum, pageSize);
    }

    /** 管理端：订单详情 */
    @GetMapping("/mgr/order/getdetail.do")
    public Order mgrDetail(@RequestParam Long orderNo) {
        // TODO: 判断是否为管理员
        return orderService.getDetail(orderNo);
    }

    /* =======================================================
     *                     用户端接口
     * ======================================================= */

    /** 用户端：创建订单 */
    @PostMapping("/order/createorder.do")
    public Order userCreate(@RequestParam Integer addrId) {
        Integer uid = (Integer) session.getAttribute("uid");
        // TODO: 未登录处理
        return orderService.createOrder(uid, addrId);
    }

    /** 用户端：订单分页列表 */
    @GetMapping("/order/getlist.do")
    public PageResult<Order> userList(@RequestParam(defaultValue = "1")  int pageNo,
                                      @RequestParam(defaultValue = "10") int pageSize,
                                      @RequestParam(required = false)    Integer status) {
        Integer uid = (Integer) session.getAttribute("uid");
        return orderService.getUserOrders(uid, status, pageNo, pageSize);
    }

    /** 用户端：订单详情 */
    @GetMapping("/order/getdetail.do")
    public Order userDetail(@RequestParam Long orderNo) {
        // 如需校验订单归属可在 Service 内处理
        return orderService.getDetail(orderNo);
    }

    /** 用户端：确认收货 */
    @PostMapping("/order/confirmreceipt.do")
    public String userConfirm(@RequestParam Long orderNo) {
        Integer uid = (Integer) session.getAttribute("uid");
        orderService.confirmReceipt(uid, orderNo);
        return "订单已确认收货！";
    }

    /** 用户端：取消订单 */
    @PostMapping("/order/cancelorder.do")
    public String userCancel(@RequestParam Long orderNo) {
        Integer uid = (Integer) session.getAttribute("uid");
        orderService.cancelOrder(uid, orderNo);
        return "该订单已经取消！";
    }
}