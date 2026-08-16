package com.amazon.dao;

import com.amazon.model.Order;
import com.amazon.model.OrderItem;
import com.amazon.model.Product;
import com.amazon.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int createOrder(Order order) {
        String sql = "INSERT INTO orders (customer_id, total_amount, order_status) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getCustomerId());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getOrderStatus());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean addOrderItem(OrderItem item) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getPrice());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int placeOrder(Order order, List<Product> products) {
        String insertOrder = "INSERT INTO orders (customer_id, total_amount, order_status) VALUES (?, ?, ?)";
        String checkStock = "SELECT stock FROM products WHERE product_id = ? FOR UPDATE";
        String updateStock = "UPDATE products SET stock = stock - ? WHERE product_id = ? AND stock >= ?";
        String insertItem = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getCustomerId());
                ps.setDouble(2, order.getTotalAmount());
                ps.setString(3, order.getOrderStatus());
                ps.executeUpdate();
            }

            int orderId;
            try (PreparedStatement ps = con.prepareStatement("SELECT LAST_INSERT_ID()")) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return -1;
                    }
                    orderId = rs.getInt(1);
                }
            }

            for (Product product : products) {
                try (PreparedStatement ps = con.prepareStatement(checkStock)) {
                    ps.setInt(1, product.getProductId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next() || rs.getInt("stock") < product.getCartQuantity()) {
                            con.rollback();
                            return -1;
                        }
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(updateStock)) {
                    ps.setInt(1, product.getCartQuantity());
                    ps.setInt(2, product.getProductId());
                    ps.setInt(3, product.getCartQuantity());
                    if (ps.executeUpdate() != 1) {
                        con.rollback();
                        return -1;
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(insertItem)) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, product.getProductId());
                    ps.setInt(3, product.getCartQuantity());
                    ps.setDouble(4, product.getPrice());
                    ps.executeUpdate();
                }
            }

            con.commit();
            return orderId;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Order> getOrdersByCustomer(int customerId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, customer_id, order_date, total_amount, order_status " +
                "FROM orders WHERE customer_id = ? ORDER BY order_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setOrderId(rs.getInt("order_id"));
                    order.setCustomerId(rs.getInt("customer_id"));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setOrderStatus(rs.getString("order_status"));
                    orders.add(order);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }
}
