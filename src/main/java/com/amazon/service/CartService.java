package com.amazon.service;

import com.amazon.dao.CartDAO;
import com.amazon.model.CartItem;
import com.amazon.model.Product;

import java.util.List;

public class CartService {

    private final CartDAO cartDAO;

    public CartService() {
        cartDAO = new CartDAO();
    }

    public int createCart(int customerId) {
        return cartDAO.getOrCreateCart(customerId);
    }

    public boolean addToCart(int customerId, int productId, int quantity) {
        return cartDAO.addToCart(customerId, productId, quantity);
    }

    public boolean addToCart(CartItem item) {
        return cartDAO.addToCart(item);
    }

    public List<Product> getCartProducts(int customerId) {
        return cartDAO.getCartProducts(customerId);
    }

    public boolean clearCart(int customerId) {
        return cartDAO.clearCart(customerId);
    }
}
