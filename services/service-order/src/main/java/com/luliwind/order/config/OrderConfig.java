package com.luliwind.order.config;

import feign.Logger;
import feign.Retryer;
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

    /**
     * 开启feign日志
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    //@Bean
    Retryer retryer() {
        return new Retryer.Default();//默认是每次间隔100ms,最多发送五次
    }
}
