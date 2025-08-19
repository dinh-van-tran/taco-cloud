package com.example.tacocloud.web;

import com.example.tacocloud.User;
import com.example.tacocloud.data.UserRepository;
import com.example.tacocloud.data.service.OrderAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/admin")
class AdminController {
    private final OrderAdminService orderAdminService;
    private final UserRepository userRepository;

    public AdminController(OrderAdminService orderAdminService, UserRepository userRepository) {
        this.orderAdminService = orderAdminService;
        this.userRepository = userRepository;
    }

    @ModelAttribute(name = "user")
    public User user(Principal principal) {
        if (principal == null) {
            return null; // No user is authenticated
        }

        String username = principal.getName();
        return userRepository.findByUsername(username);
    }

    @GetMapping
    public String showAdminPage() {
        return "admin";
    }

    @PostMapping("/deleteOrders")
    public String deleteAllOrders() {
        orderAdminService.deleteAllOrders();
        return "redirect:/admin";
    }
}
