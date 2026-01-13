package com.luliwind.order.service.impl;

import com.luliwind.order.bean.Order;
import com.luliwind.order.service.OrderService;
import com.luliwind.product.bean.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;


    @Override
    public Order createOrder(Long productId, Long userId) {
        Product product = getProductFromRemote(productId);
        Order order = new Order();
        order.setUserId(userId);
        order.setId(1L);
        // 总金额
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setAddress("我家");
        order.setNickName("zhangsan");
        //远程查询商品列表
        order.setProductList(Arrays.asList(product));
        return order;
    }

    private Product getProductFromRemote(Long productId){
        //1.获取商品服务所在的所有Ip+port
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        ServiceInstance instance = instances.get(0);
        //远程url
        String url = "http://" + instance.getHost() + ":" + instance.getPort() +"/product/" + productId;
        //log.info("远程请求：{}",url);
        //2、给远程发送请求
         Product forObject = restTemplate.getForObject(url, Product.class);
        return forObject;
    }
}
