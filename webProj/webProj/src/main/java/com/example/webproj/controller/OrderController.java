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
        this.session = session;
    }

    /* =======================================================
     *                     管理端接口
     * ======================================================= */

    /** 管理端：订单列表（不分页） */
    @GetMapping("/mgr/order/findorders_nopages.do")
    public Map<String, Object> mgrListNoPages(@RequestParam(required = false) Long orderNo) {
        Map<String, Object> result= new HashMap<>();
        List<Order>orders = orderService.findOrdersNoPages(orderNo);
        if(orders!=null)
        {
            result.put("status", 0);
            result.put("data", orders);
            return result;
        }
        result.put("status", 1);
        result.put("msg", "没有订单信息");
        return result;
    }

    /** 管理端：根据订单号分页搜索 */
    @GetMapping("/mgr/order/search.do")
    public Map<String, Object> mgrSearch(@RequestParam Long orderNo,
                                       @RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> result= new HashMap<>();
        List<Order>orders = orderService.searchOrders(orderNo, pageNum, pageSize);
        if(orders!=null)
        {
            Map<String, Object> resultVo = new HashMap<>();
            resultVo.put("pageNum", 1);
            resultVo.put("pageSize", 10);
            resultVo.put("totalRecord", 1);
            resultVo.put("totalPage", 1);
            resultVo.put("startIndex", 0);
            resultVo.put("prePage", 1);
            resultVo.put("nextPage", 1);
            resultVo.put("data", orders);

            result.put("status", 0);
            result.put("data", orders);
            return result;
        }
        result.put("status", 1);
        result.put("data", "resultVo");
        return result;
    }

    /** 管理端：所有订单查询（分页） */
    @PostMapping("/mgr/order/findorders.do")
    public Map<String, Object> mgrListPaging(@RequestParam(defaultValue = "1")  int pageNum,
                                           @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<Order> pageResult = orderService.findOrdersPaging(pageNum, pageSize);

        Map<String, Object> result= new HashMap<>();
        Map<String, Object> resultVo= new HashMap<>();
        resultVo.put("pageNum", pageResult.getPageNum());
        resultVo.put("pageSize", pageResult.getPageSize());
        resultVo.put("totalRecord", pageResult.getTotalRecord());
        resultVo.put("totalPage", pageResult.getTotalPage());
        resultVo.put("startIndex", pageResult.getStartIndex());
        resultVo.put("prePage", pageResult.getPageNum()-1);
        resultVo.put("nextPage", pageResult.getPageNum()+1);
        resultVo.put("data", pageResult.getData());

        result.put("status", 0);
        result.put("data", resultVo);
        return result;
    }

    /** 管理端：订单详情（不分页） */
    @GetMapping("/mgr/order/getdetail.do")
    public Map<String, Object> mgrDetail(@RequestParam Long orderNo) {
        Order order= orderService.getDetail(orderNo);
        Map<String, Object> result= new HashMap<>();
        result.put("status", 0);
        result.put("data", order);
        return result;
    }

    /* =======================================================
     *                     用户端接口
     * ======================================================= */

    /** 用户端：创建订单 */
    @PostMapping("/order/createorder.do")
    public Order userCreate(@RequestParam Integer addrId) {
        Integer uid = (Integer) session.getAttribute("uid");
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
    public Map<String, Object> userConfirm(@RequestParam Long orderNo) {
        Integer uid = (Integer) session.getAttribute("uid");
        orderService.confirmReceipt(orderNo);
        Map<String, Object> result= new HashMap<>();

        result.put("status", 0);
        result.put("msg","订单已确认收货！");
        return result;
    }

    /** 用户端：取消订单 */
    @PostMapping("/order/cancelorder.do")
    public String userCancel(@RequestParam Long orderNo) {
        Integer uid = (Integer) session.getAttribute("uid");
        orderService.cancelOrder(uid, orderNo);
        return "该订单已经取消！";
    }
}