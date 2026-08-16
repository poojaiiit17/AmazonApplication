package com.amazon.service;

import com.amazon.dao.CustomerDAO;

public class CustomerService {

    private CustomerDAO customerDAO;

    public CustomerService() {
        customerDAO = new CustomerDAO();
    }

    public boolean login(String email, String password) {
        return customerDAO.login(email, password);
    }

    public int getCustomerId(String email, String password) {
        return customerDAO.getCustomerId(email, password);
    }
}
