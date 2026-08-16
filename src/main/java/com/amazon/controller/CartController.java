package com.amazon.controller;

import com.amazon.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CartController {

    private final CartService cartService = new CartService();

    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam("productId") int productId,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            HttpSession session,
            Model model) {

        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) return "redirect:/customer-login";

        if (quantity < 1) quantity = 1;

        boolean result = cartService.addToCart(customerId, productId, quantity);
        model.addAttribute("message", result ? "Product added to cart!" : "Failed to add product to cart.");
        model.addAttribute("products", cartService.getCartProducts(customerId));
        return "cart";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) return "redirect:/customer-login";

        model.addAttribute("products", cartService.getCartProducts(customerId));
        return "cart";
    }
}
