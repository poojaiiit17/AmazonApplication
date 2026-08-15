package com.amazon.service;

import com.amazon.dao.SellerDAO;

public class SellerService {

    private SellerDAO sellerDAO;

    public SellerService() {
        sellerDAO = new SellerDAO();
    }

    public boolean login(String email, String password) {

        return sellerDAO.login(email, password);
    }

    public int getSellerId(String email, String password) {

        return sellerDAO.getSellerId(email, password);
    }
}