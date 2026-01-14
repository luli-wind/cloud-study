package com.luliwind.order.feign.fallback;

import com.luliwind.order.feign.ProductFeignClient;
import com.luliwind.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * FeignClient兜底回调，FailBack；
 * 可以返回缓存，假数据，默认数据等
 */
@Slf4j
@Component
public class ProductFeignClientFallback implements ProductFeignClient {

    @Override
    public Product getProductById(Long id) {
        log.info("ProductFeignClientFallback 兜底数据--------------------------");
        return new Product(){{
            setId(-1L);
            setProductName("ProductFeignClientFallback 兜底数据");
            setNum(-1);
            setPrice(BigDecimal.ZERO);
        }};
    }
}