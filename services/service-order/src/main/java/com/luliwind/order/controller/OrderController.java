package com.luliwind.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.luliwind.order.bean.Order;
import com.luliwind.order.properties.OrderProperties;
import com.luliwind.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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


    //创建秒杀订单
    @GetMapping("/seckill")
    // 自定义流控埋点
    @SentinelResource(value = "seckill-order", fallback =  "seckillFallback")
    public Order createKillOrder(@RequestParam( value = "userId",required = false)  Long userId,
                                 @RequestParam("productId") Long productId) {
        return orderService.createOrder(productId, userId);
    }

    public Order seckillFallback(Long userId, Long productId, Throwable e) {
        log.info("fallback.....................");
        Order order = new Order() {{
            setId(-1L);
            setAddress("商品已下架");
        }};

        return order;
    }

    /**
     * 写数据
     */
    @GetMapping("/writeDb")
    public String writeData() {
        return "writeDb success";
    }
    /**
     * 读数据
     */
    @GetMapping("/readDb")
    public String readData() {
        log.info("readDb success......");
        return "readDb success";
    }
}
