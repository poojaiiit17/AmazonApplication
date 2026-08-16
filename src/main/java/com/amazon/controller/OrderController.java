package com.amazon.controller;

import com.amazon.model.Order;
import com.amazon.model.Payment;
import com.amazon.model.Product;
import com.amazon.service.CartService;
import com.amazon.service.OrderService;
import com.amazon.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
public class OrderController {

    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();
    private final PaymentService paymentService = new PaymentService();

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) return "redirect:/customer-login";

        List<Product> products = cartService.getCartProducts(customerId);
        if (products.isEmpty()) return "redirect:/cart";

        double total = products.stream().mapToDouble(Product::getCartTotal).sum();
        model.addAttribute("products", products);
        model.addAttribute("total", total);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(@RequestParam("paymentMethod") String paymentMethod,
                             HttpSession session, Model model) {
        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) return "redirect:/customer-login";

        List<Product> products = cartService.getCartProducts(customerId);
        if (products.isEmpty()) return "redirect:/cart";

        double total = products.stream().mapToDouble(Product::getCartTotal).sum();

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setTotalAmount(total);
        order.setOrderStatus("CONFIRMED");

        int orderId = orderService.placeOrder(order, products);
        if (orderId == -1) {
            model.addAttribute("message", "Order failed. Please check product stock.");
            model.addAttribute("products", products);
            model.addAttribute("total", total);
            return "checkout";
        }

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setAmount(total);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus("SUCCESS");
        payment.setTransactionId("TXN-" + UUID.randomUUID());

        if (!paymentService.makePayment(payment)) {
            model.addAttribute("message", "Order created but payment failed.");
            model.addAttribute("orderId", orderId);
            return "order-success";
        }

        cartService.clearCart(customerId);
        model.addAttribute("orderId", orderId);
        model.addAttribute("total", total);
        return "order-success";
    }

    @GetMapping("/orders")
    public String orderHistory(HttpSession session, Model model) {
        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) return "redirect:/customer-login";

        model.addAttribute("orders", orderService.getOrdersByCustomer(customerId));
        return "orders";
    }
}
