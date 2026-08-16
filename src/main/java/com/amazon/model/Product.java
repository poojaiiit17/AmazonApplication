package com.amazon.model;

public class Product {

    private int productId;
    private int sellerId;
    private String productName;
    private String description;
    private double price;
    private int stock;
    private String category;
    private int cartQuantity;

    public Product() {}

    public Product(int productId, int sellerId, String productName,
                   String description, double price, int stock, String category) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getCartQuantity() { return cartQuantity; }
    public void setCartQuantity(int cartQuantity) { this.cartQuantity = cartQuantity; }
    public double getCartTotal() { return price * cartQuantity; }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", sellerId=" + sellerId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                '}';
    }
}