package com.Webapp.BikeRentalSystem.Model;

import java.io.IOException;
import java.util.ArrayList;

public interface PaymentRepository {  //CRUD operations
    public void save(Payment payment) throws IOException;  // Create
    ArrayList<Payment> findAll() throws IOException;  // Read
}
