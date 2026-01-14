package com.luliwind.order.controller;

import com.luliwind.order.bean.Order;
import com.luliwind.order.properties.OrderProperties;
import com.luliwind.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//@RefreshScope //自动刷新配置属性
@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

//    @Value("${order.timeout}")
//    String orderTimeout;
//    @Value("${order.auto-confirm}")
//    String orderAutoConfirm;

    @Autowired
    OrderProperties orderProperties;

    @GetMapping("/config")
    public String config(){
        return "order"+"--"
                +orderProperties.getTimeout()+"--"
                + orderProperties.getAutoConfirm()+"--"
                +orderProperties.getDbUrl();
    }


    //创建订单
    @GetMapping("/create")
    public Order createOrder(@RequestParam("userId") Long userId,
                             @RequestParam("productId") Long productId){
        Order order = orderService.createOrder(productId,userId);
        return order;
    }
}
