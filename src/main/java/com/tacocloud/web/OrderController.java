package com.tacocloud.web;

import com.tacocloud.TacoOrder;
import com.tacocloud.User;
import com.tacocloud.data.OrderRepository;
import com.tacocloud.messaging.OrderMessagingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
class OrderController {

    private final OrderRepository orderRepository;
    private final OrderMessagingService orderMessagingService;

    @Autowired
    public OrderController(OrderRepository orderRepository, OrderMessagingService orderMessagingService) {
        this.orderRepository = orderRepository;
        this.orderMessagingService = orderMessagingService;
    }

    @GetMapping("/current")
    public String orderForm(@AuthenticationPrincipal User user, @ModelAttribute TacoOrder order) {
        if (order.getDeliveryName() == null) {
            order.setDeliveryName(user.getFullName());
        }

        if (order.getDeliveryStreet() == null) {
            order.setDeliveryStreet(user.getStreet());
        }

        if (order.getDeliveryCity() == null) {
            order.setDeliveryCity(user.getCity());
        }

        if (order.getDeliveryState() == null) {
            order.setDeliveryState(user.getState());
        }

        if (order.getDeliveryZip() == null) {
            order.setDeliveryZip(user.getZip());
        }

        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid TacoOrder order, Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal User user) {
        if (errors.hasErrors()) {
            return "orderForm";
        }

        order.setUser(user);

        log.info("Processing order: {}", order);

        var savedOrder = orderRepository.save(order);
        sessionStatus.setComplete();

        orderMessagingService.sendOrder(savedOrder);

        return "redirect:/";
    }

    @PostAuthorize("hasAuthority('ROLE_ADMIN') || returnObject.user.username == authentication.name")
    public TacoOrder getOrder(long id) {
        return orderRepository.findById(id).orElse(null);
    }
}
