package com.amazon.service;

import com.amazon.dao.CartDAO;
import com.amazon.model.CartItem;

public class CartService {

    private CartDAO cartDAO;

    public CartService() {
        cartDAO = new CartDAO();
    }

    public int createCart(int customerId) {

        return cartDAO.createCart(customerId);
    }

    public boolean addToCart(CartItem item) {

        return cartDAO.addToCart(item);
    }
}