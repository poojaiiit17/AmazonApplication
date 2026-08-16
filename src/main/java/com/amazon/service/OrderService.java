package com.amazon.service;

import com.amazon.dao.OrderDAO;
import com.amazon.model.Order;
import com.amazon.model.OrderItem;
import com.amazon.model.Product;

import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO;

    public OrderService() {
        orderDAO = new OrderDAO();
    }

    public int createOrder(Order order) {
        return orderDAO.createOrder(order);
    }

    public boolean addOrderItem(OrderItem item) {
        return orderDAO.addOrderItem(item);
    }

    public int placeOrder(Order order, List<Product> products) {
        return orderDAO.placeOrder(order, products);
    }

    public List<Order> getOrdersByCustomer(int customerId) {
        return orderDAO.getOrdersByCustomer(customerId);
    }
}
