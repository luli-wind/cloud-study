package com.luliwind.gateway.predicate;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.cloud.gateway.handler.predicate.QueryRoutePredicateFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


@Component
public class VipRoutePredicateFactory extends AbstractRoutePredicateFactory<VipRoutePredicateFactory.Config> {

    public VipRoutePredicateFactory() {
        super(Config.class);
    }

    public Predicate<ServerWebExchange> apply(Config config) {
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                ServerHttpRequest request = serverWebExchange.getRequest();
                String first = request.getQueryParams().getFirst(config.getParam());
                if (StringUtils.hasText(first) && first.equals(config.val)) {
                    return true;
                }
                return false;
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("param","val");
    }

    /**
     * 配置的参数
     */
    @Validated
    public static class Config {
        private @NotEmpty String param;
        private @NotEmpty String val;

        public @NotEmpty String getParam() {
            return param;
        }
        public @NotEmpty String getVal() {
            return val;
        }

        public void setParam(@NotEmpty String param) {
            this.param = param;
        }

        public void setVal(@NotEmpty String val) {
            this.val = val;
        }

    }
}
