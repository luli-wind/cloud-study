package com.luliwind.order.feign;

import com.luliwind.order.feign.fallback.ProductFeignClientFallback;
import com.luliwind.product.bean.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "service-product",fallback = ProductFeignClientFallback.class)//feign客户端
public interface ProductFeignClient {

    @GetMapping("/product/{id}")
    Product getProductById(@PathVariable("id") Long id);
}
