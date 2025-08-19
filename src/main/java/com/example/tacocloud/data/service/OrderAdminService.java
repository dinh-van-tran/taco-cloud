package com.example.tacocloud.data.service;

import com.example.tacocloud.data.OrderRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class OrderAdminService {
    private final OrderRepository orderRepository;

    public OrderAdminService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}
