package com.amazon.service;

import com.amazon.dao.OrderDAO;
import com.amazon.model.Order;
import com.amazon.model.OrderItem;

public class OrderService {

    private OrderDAO orderDAO;

    public OrderService() {
        orderDAO = new OrderDAO();
    }

    public int createOrder(Order order) {

        return orderDAO.createOrder(order);
    }

    public boolean addOrderItem(OrderItem item) {

        return orderDAO.addOrderItem(item);
    }
}