package com.Webapp.BikeRentalSystem.dto;

import jakarta.validation.constraints.Size;

public class UserUpdateDTO {

    private String name;
    private String phone;
    private String address;

    @Size(min = 6, message = "New password must be at least 6 characters")
    private String newPassword;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
