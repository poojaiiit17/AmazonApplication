package com.amazon.dao;

import com.amazon.model.Cart;
import com.amazon.model.CartItem;
import com.amazon.util.DBConnection;

import java.sql.*;

public class CartDAO {

    public int createCart(int customerId) {

        String sql = "INSERT INTO cart (customer_id) VALUES (?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            ps.setInt(1, customerId);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean addToCart(CartItem item) {

        String sql = "INSERT INTO cart_items " +
                "(cart_id, product_id, quantity) VALUES (?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, item.getCartId());
            ps.setInt(2, item.getProductId());
            ps.setInt(3, item.getQuantity());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}