package com.luliwind.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class OnceTokenGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {
    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                //每次响应之前、添加一个一次性令牌，支持UUID、jwt等各种格式
                return chain.filter(exchange).then(Mono.fromRunnable(()->{
                    String value=config.getValue();
                    if("uuid".equalsIgnoreCase(value)){
                        value=java.util.UUID.randomUUID().toString();
                    }
                    if("jwt".equalsIgnoreCase(value)){
                        value="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyNDI2MjJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c\n";
                    }
                    exchange.getResponse().getHeaders().add(config.getName(),value);
                }));
            }
        };
    }
}
