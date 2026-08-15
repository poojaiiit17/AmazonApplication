package com.amazon.dao;

import com.amazon.model.Payment;
import com.amazon.util.DBConnection;

import java.sql.*;

public class PaymentDAO {

    public boolean makePayment(Payment payment) {

        String sql = "INSERT INTO payments " +
                "(order_id, amount, payment_method, payment_status, transaction_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, payment.getOrderId());
            ps.setDouble(2, payment.getAmount());
            ps.setString(3, payment.getPaymentMethod());
            ps.setString(4, payment.getPaymentStatus());
            ps.setString(5, payment.getTransactionId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}