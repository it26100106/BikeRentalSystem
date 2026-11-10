package com.Webapp.BikeRentalSystem.controller;

import com.Webapp.BikeRentalSystem.dto.LoginDTO;
import com.Webapp.BikeRentalSystem.dto.RegisterDTO;
import com.Webapp.BikeRentalSystem.dto.UserUpdateDTO;
import com.Webapp.BikeRentalSystem.model.User;
import com.Webapp.BikeRentalSystem.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ---------- REGISTER ----------
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerDTO") RegisterDTO dto,
                           BindingResult result, Model model) {
        if (result.hasErrors()) return "user/register";
        try {
            userService.registerUser(dto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "user/register";
        }
        return "redirect:/users/login";
    }

    // ---------- LOGIN ----------
    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginDTO") LoginDTO dto,
                        BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) return "user/login";
        try {
            User user = userService.loginUser(dto);
            session.setAttribute("loggedInUser", user);
            if ("Admin".equals(user.getRole())) return "redirect:/";
            return "redirect:/users/profile";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "user/login";
        }
    }

    // ---------- LOGOUT ----------
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logOutUser(session);
        return "redirect:/users/login";
    }

    // ---------- PROFILE ----------
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/users/login";
        model.addAttribute("user", user);
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        model.addAttribute("userUpdateDTO", dto);
        return "user/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute("userUpdateDTO") UserUpdateDTO dto,
                                BindingResult result, HttpSession session,
                                Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/users/login";
        if ("Admin".equals(user.getRole())) return "redirect:/users/profile"; // admin details are read-only
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "user/profile";
        }
        try {
            User updated = userService.updateUser(user.getUserId(), dto, user);
            session.setAttribute("loggedInUser", updated);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", user);
            return "user/profile";
        }
        return "redirect:/users/profile";
    }

    // ---------- DELETE ----------
    @PostMapping("/delete")
    public String deleteUser(@RequestParam(required = false) String id,
                             HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) return "redirect:/users/login";
        if ("Admin".equals(user.getRole()) && id == null) return "redirect:/";
        String targetId = (id != null && "Admin".equals(user.getRole()))
                ? id : user.getUserId();
        userService.deleteUser(targetId, user);
        if (targetId.equals(user.getUserId())) {
            session.invalidate();
            return "redirect:/users/register";
        }
        return "redirect:/users/profile";
    }
}

