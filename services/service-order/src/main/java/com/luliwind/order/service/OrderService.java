package com.luliwind.order.service;

import com.luliwind.order.bean.Order;

public interface OrderService {
    Order createOrder(Long productId, Long userId);
}
