package com.Webapp.BikeRentalSystem.Model;

public class PaymentException extends Exception { //custom exception

    public PaymentException(String message) {
        super(message);
    }
}
