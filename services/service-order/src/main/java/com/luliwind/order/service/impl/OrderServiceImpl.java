package com.luliwind.order.service.impl;

import com.luliwind.order.bean.Order;
import com.luliwind.order.feign.ProductFeignClient;
import com.luliwind.order.service.OrderService;
import com.luliwind.product.bean.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Autowired
    ProductFeignClient productFeignClient;


    @Override
    public Order createOrder(Long productId, Long userId) {
        //Product product = getProductFromRemoteWithLoadBalanceAnnotation(productId);
        //使用feign实现远程调用
        Product product = productFeignClient.getProductById(productId);
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

    //一、
    private Product getProductFromRemote(Long productId){
        //1.获取商品服务所在的所有Ip+port
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        ServiceInstance instance = instances.get(0);
        //远程url
        String url = "http://" + instance.getHost() + ":" + instance.getPort() +"/product/" + productId;
        log.info("远程请求：{}",url);
        //2、给远程发送请求
         Product forObject = restTemplate.getForObject(url, Product.class);
        return forObject;
    }

    //二、负载均衡
    private Product getProductFromRemoteWithLoadBalance(Long productId){
        //1.获取商品服务所在的所有Ip+port
        ServiceInstance instance = loadBalancerClient.choose("service-product");
        //远程url
        String url = "http://" + instance.getHost() + ":" + instance.getPort() +"/product/" + productId;
        log.info("远程请求：{}",url);
        //2、给远程发送请求
        Product forObject = restTemplate.getForObject(url, Product.class);
        return forObject;
    }

    //三、基于注解的负载均衡
    private Product getProductFromRemoteWithLoadBalanceAnnotation(Long productId){
        //url会被动态替换
        String url ="http://service-product/product/" + productId;
        log.info("远程请求：{}",url);;
        //2、给远程发送请求
        Product forObject = restTemplate.getForObject(url, Product.class);
        return forObject;
    }
}
