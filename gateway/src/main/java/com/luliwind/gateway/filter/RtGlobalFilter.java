package com.luliwind.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class RtGlobalFilter implements GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long start = System.currentTimeMillis();
        log.info("请求【{}】开始：时间：{}",
                exchange.getRequest().getURI().toString(),
                start);

        //以上是前置逻辑
        Mono<Void> filter = chain.filter(exchange)
                .doFinally((result)->{
                    long end = System.currentTimeMillis();
                    log.info("请求【{}】结束：时间：{},耗时为{}ms",
                            exchange.getRequest().getURI().toString(),
                            end,end-start);
                });//放行


        return filter;
    }
}
