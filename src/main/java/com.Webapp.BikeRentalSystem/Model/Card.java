package com.Webapp.BikeRentalSystem.Model;

import java.time.LocalDate;

public class Card extends Payment {

    public Card(String paymentId, String bookingId, String userId, double amount, LocalDate paymentDate) {
        super(paymentId, bookingId, userId, amount, paymentDate, "Pending", "Card");
    }

    public void processPayment() throws PaymentException {
        setStatus("Paid");
    }
}
