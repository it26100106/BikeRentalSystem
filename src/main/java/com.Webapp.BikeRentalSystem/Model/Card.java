package com.Webapp.BikeRentalSystem.Model;

import java.time.LocalDate;
import java.time.YearMonth;

public class Card extends Payment {
    private String cardholderName = "";
    private String cardNumber = "";
    private String expiry = "";
    private String cvv = "";

    public Card(String paymentId, String bookingId, String userId, double amount, LocalDate paymentDate) {
        super(paymentId, bookingId, userId, amount, paymentDate, "Pending", "Card");
    }

    public void setCardDetails(String cardholderName, String cardNumber, String expiry, String cvv) {
        this.cardholderName = cardholderName.trim();
        this.cardNumber = cardNumber.trim();
        this.expiry = expiry.trim();
        this.cvv = cvv.trim();
    }

    public void processPayment() throws PaymentException {
        if (cardholderName.isEmpty()) {
            throw new PaymentException("Cardholder name is required.");
        }
        if (cardNumber.length() != 16) {
            throw new PaymentException("Card number must be 16 digits.");
        }
        setStatus("Paid");
    }
}
