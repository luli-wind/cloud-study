package com.luliwind.product.controller;

import com.luliwind.product.bean.Product;
import com.luliwind.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    //查询商品
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long productId,
                              HttpServletRequest request) {
        String header = request.getHeader("X-Token");
        System.out.println("hello....token");
        Product product = productService.getProductById(productId);
        return product;
    }
}
