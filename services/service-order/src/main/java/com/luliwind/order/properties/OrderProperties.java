package com.luliwind.order.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "order")//配置批量绑定，无需refreshScope就可以自动刷新
public class OrderProperties {


    String timeout;

    String autoConfirm;

    String dbUrl;
}
