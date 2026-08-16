package com.amazon.controller;

import com.amazon.service.CartService;
import com.amazon.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerController {

    private final CustomerService customerService = new CustomerService();
    private final CartService cartService = new CartService();

    @GetMapping("/customer-login")
    public String customerLoginPage() {
        return "customer-login";
    }

    @PostMapping("/customer-login")
    public String customerLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session,
            Model model) {

        boolean result = customerService.login(email, password);

        if (!result) {
            model.addAttribute("message", "Invalid Email or Password");
            return "customer-login";
        }

        int customerId = customerService.getCustomerId(email, password);
        session.setAttribute("customerId", customerId);
        cartService.createCart(customerId);

        return "redirect:/customer-dashboard";
    }

    @GetMapping("/customer-dashboard")
    public String customerDashboard(HttpSession session) {
        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) return "redirect:/customer-login";
        return "customer-dashboard";
    }
}
