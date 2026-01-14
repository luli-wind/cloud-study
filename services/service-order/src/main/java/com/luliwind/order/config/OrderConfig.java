package com.luliwind.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OrderConfig {

    @LoadBalanced//基于注解的负载均衡
    @Bean
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
