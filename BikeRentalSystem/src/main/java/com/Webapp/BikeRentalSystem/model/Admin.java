package com.Webapp.BikeRentalSystem.model;

/** An admin account read from admins.txt (one line per admin: email|password). */
public class Admin {
    private final String email;
    private final String password;

    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }
    public String getPassword() { return password; }
}
