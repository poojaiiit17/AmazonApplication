package com.amazon.controller;

import com.amazon.service.SellerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private SellerService sellerService = new SellerService();

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            HttpSession session) {

        System.out.println("Email received: " + email);

        boolean result = sellerService.login(email, password);

        if (result) {

            // Get the ID of the logged-in seller
            int sellerId = sellerService.getSellerId(email, password);

            // Store seller ID in session
            session.setAttribute("sellerId", sellerId);

            System.out.println("Seller ID: " + sellerId);

            model.addAttribute(
                    "message",
                    "Seller Login Successful!"
            );

            return "seller-dashboard";

        } else {

            model.addAttribute(
                    "message",
                    "Invalid Email or Password"
            );

            return "login";
        }
    }
}