package com.amazon.dao;

import com.amazon.model.CartItem;
import com.amazon.model.Product;
import com.amazon.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartDAO {

    public int getOrCreateCart(int customerId) {
        String findSql = "SELECT cart_id FROM cart WHERE customer_id = ?";
        String insertSql = "INSERT INTO cart (customer_id) VALUES (?)";

        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(findSql)) {
                ps.setInt(1, customerId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return rs.getInt("cart_id");
                }
            }

            try (PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, customerId);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean addToCart(int customerId, int productId, int quantity) {
        int cartId = getOrCreateCart(customerId);
        if (cartId == -1) return false;

        String findSql = "SELECT cart_item_id FROM cart_items WHERE cart_id = ? AND product_id = ?";
        String updateSql = "UPDATE cart_items SET quantity = quantity + ? WHERE cart_item_id = ?";
        String insertSql = "INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(findSql)) {
                ps.setInt(1, cartId);
                ps.setInt(2, productId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        try (PreparedStatement ups = con.prepareStatement(updateSql)) {
                            ups.setInt(1, quantity);
                            ups.setInt(2, rs.getInt("cart_item_id"));
                            return ups.executeUpdate() > 0;
                        }
                    }
                }
            }

            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, cartId);
                ps.setInt(2, productId);
                ps.setInt(3, quantity);
                return ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Product> getCartProducts(int customerId) {
        List<Product> products = new ArrayList<>();

        // Step 1: Find the cart of this customer.
        int cartId = getOrCreateCart(customerId);
        if (cartId == -1) return products;

        // Step 2: Get product IDs and quantities from cart_items.
        String cartItemsSql = "SELECT product_id, quantity FROM cart_items WHERE cart_id = ?";

        // Keep quantity separately because product details are stored in products table.
        Map<Integer, Integer> quantities = new HashMap<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(cartItemsSql)) {

            ps.setInt(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    quantities.put(rs.getInt("product_id"), rs.getInt("quantity"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return products;
        }

        // Step 3: Fetch each product separately. No JOIN is used.
        String productSql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(productSql)) {

            for (Map.Entry<Integer, Integer> entry : quantities.entrySet()) {
                ps.setInt(1, entry.getKey());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Product p = new Product();
                        p.setProductId(rs.getInt("product_id"));
                        p.setSellerId(rs.getInt("seller_id"));
                        p.setProductName(rs.getString("product_name"));
                        p.setDescription(rs.getString("description"));
                        p.setPrice(rs.getDouble("price"));
                        p.setStock(rs.getInt("stock"));
                        p.setCategory(rs.getString("category"));
                        p.setCartQuantity(entry.getValue());
                        products.add(p);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    public boolean clearCart(int customerId) {
        String sql = "DELETE ci FROM cart_items ci " +
                "JOIN cart c ON ci.cart_id = c.cart_id " +
                "WHERE c.customer_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int createCart(int customerId) {
        return getOrCreateCart(customerId);
    }

    public boolean addToCart(CartItem item) {
        return addToCart(item.getCartId(), item.getProductId(), item.getQuantity());
    }
}
