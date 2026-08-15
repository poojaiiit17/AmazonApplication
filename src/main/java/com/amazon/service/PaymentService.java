package com.amazon.service;

import com.amazon.dao.PaymentDAO;
import com.amazon.model.Payment;

import java.util.UUID;

public class PaymentService {

    private PaymentDAO paymentDAO;

    public PaymentService() {
        paymentDAO = new PaymentDAO();
    }

    public boolean makePayment(Payment payment) {

        // Dummy payment
        payment.setPaymentStatus("SUCCESS");

        // Generate dummy transaction ID
        payment.setTransactionId(
                "TXN-" + UUID.randomUUID()
        );

        return paymentDAO.makePayment(payment);
    }
}