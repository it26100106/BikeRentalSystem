package com.Webapp.BikeRentalSystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Home page: templates/home.html
    @GetMapping("/")
    public String home() {
        return "home";
    }
}