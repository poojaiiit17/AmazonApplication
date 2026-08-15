package com.amazon.controller;

import com.amazon.model.Product;
import com.amazon.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    private ProductService productService = new ProductService();

    // Open Add Product page
    @GetMapping("/add-product")
    public String addProductPage(HttpSession session) {

        Integer sellerId =
                (Integer) session.getAttribute("sellerId");

        if (sellerId == null) {
            return "redirect:/login";
        }

        return "add-product";
    }

    // Handle Add Product form
    @PostMapping("/add-product")
    public String addProduct(
            @RequestParam("productName") String productName,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("stock") int stock,
            @RequestParam("category") String category,
            HttpSession session,
            Model model) {

        Integer sellerId =
                (Integer) session.getAttribute("sellerId");

        if (sellerId == null) {
            return "redirect:/login";
        }

        Product product = new Product();

        product.setSellerId(sellerId);
        product.setProductName(productName);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);

        boolean result = productService.addProduct(product);

        if (result) {
            model.addAttribute(
                    "message",
                    "Product added successfully!"
            );
        } else {
            model.addAttribute(
                    "message",
                    "Failed to add product."
            );
        }

        return "add-product";
    }

    // View only logged-in seller's products
    @GetMapping("/my-products")
    public String myProducts(
            HttpSession session,
            Model model) {

        Integer sellerId =
                (Integer) session.getAttribute("sellerId");

        if (sellerId == null) {
            return "redirect:/login";
        }

        List<Product> products =
                productService.getProductsBySeller(sellerId);

        model.addAttribute("products", products);

        return "my-products";
    }
}