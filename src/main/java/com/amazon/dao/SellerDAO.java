package com.amazon.dao;

import com.amazon.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SellerDAO {

    public boolean login(String email, String password) {

        String sql = "SELECT * FROM sellers WHERE email = ? AND password = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    public int getSellerId(String email, String password) {

        String sql = "SELECT seller_id FROM sellers WHERE email = ? AND password = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("seller_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }
}