package com.Webapp.BikeRentalSystem.controller;

import com.Webapp.BikeRentalSystem.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("loggedInUser")
    public User loggedInUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }
}