package com.amazon.service;

import com.amazon.dao.ProductDAO;
import com.amazon.model.Product;

import java.util.List;

public class ProductService {

    private ProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO();
    }

    public boolean addProduct(Product product) {

        return productDAO.addProduct(product);
    }

    public List<Product> getAllProducts() {

        return productDAO.getAllProducts();
    }
    public List<Product> getProductsBySeller(int sellerId) {

        return productDAO.getProductsBySeller(sellerId);
    }
}