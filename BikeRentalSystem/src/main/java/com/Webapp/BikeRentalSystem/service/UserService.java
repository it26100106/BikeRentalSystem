package com.Webapp.BikeRentalSystem.service;

import com.Webapp.BikeRentalSystem.dto.LoginDTO;
import com.Webapp.BikeRentalSystem.dto.RegisterDTO;
import com.Webapp.BikeRentalSystem.dto.UserUpdateDTO;
import com.Webapp.BikeRentalSystem.filehandling.AdminFileHandler;
import com.Webapp.BikeRentalSystem.filehandling.UserFileHandler;
import com.Webapp.BikeRentalSystem.model.Admin;
import com.Webapp.BikeRentalSystem.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserFileHandler fileHandler;
    private final AdminFileHandler adminFileHandler;

    public UserService(UserFileHandler fileHandler, AdminFileHandler adminFileHandler) {
        this.fileHandler = fileHandler;
        this.adminFileHandler = adminFileHandler;
    }

    // Register: unique email, auto-generate userId, save
    public synchronized User registerUser(RegisterDTO dto) {
        // Admin accounts cannot be created through registration (they live in admins.txt)
        if ("Admin".equalsIgnoreCase(dto.getRole()))
            throw new IllegalArgumentException("Choose Customer, Owner or Driver");

        List<User> users = fileHandler.loadUsers();

        boolean exists = users.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(dto.getEmail()))
                || adminFileHandler.loadAdmins().stream()
                .anyMatch(a -> a.getEmail().equalsIgnoreCase(dto.getEmail()));
        if (exists) throw new IllegalArgumentException("Email already registered");

        String newId = fileHandler.nextId(users);
        User u = new User(newId, dto.getName(), dto.getEmail(), dto.getPassword(),
                dto.getPhone(), dto.getAddress(), dto.getRole());
        users.add(u);
        fileHandler.saveUsers(users);
        return u;
    }

    // Login: email + password must match. Admins (admins.txt) are checked first,
    // so admins and users sign in on the same page.
    public User loginUser(LoginDTO dto) {
        String email = dto.getEmail().trim();

        for (Admin a : adminFileHandler.loadAdmins()) {
            if (a.getEmail().equalsIgnoreCase(email) && a.getPassword().equals(dto.getPassword())) {
                return new User("ADMIN", "Administrator", a.getEmail(), "", "", "", "Admin");
            }
        }

        User user = fileHandler.loadUsers().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email)
                        && u.getPassword().equals(dto.getPassword()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Old accounts that were registered as "Admin" are no longer admins: only admins.txt counts.
        if ("Admin".equals(user.getRole())) user.setRole("Customer");
        return user;
    }

    // Update: own account only, or Admin for anyone
    public synchronized User updateUser(String id, UserUpdateDTO dto, User loggedIn) {
        if (!loggedIn.getUserId().equals(id) && !"Admin".equals(loggedIn.getRole())) {
            throw new SecurityException("You can only update your own account");
        }
        List<User> users = fileHandler.loadUsers();
        User target = users.stream()
                .filter(u -> u.getUserId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (dto.getName() != null && !dto.getName().isBlank())
            target.setName(dto.getName());
        if (dto.getPhone() != null && !dto.getPhone().isBlank())
            target.setPhone(dto.getPhone());
        if (dto.getAddress() != null && !dto.getAddress().isBlank())
            target.setAddress(dto.getAddress());
        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            if (dto.getNewPassword().length() < 6)
                throw new IllegalArgumentException("Password must be at least 6 characters");
            target.setPassword(dto.getNewPassword());
        }
        fileHandler.saveUsers(users);
        return target;
    }

    // Delete: own account only, or Admin for anyone
    public synchronized void deleteUser(String id, User loggedIn) {
        if (!loggedIn.getUserId().equals(id) && !"Admin".equals(loggedIn.getRole())) {
            throw new SecurityException("You can only delete your own account");
        }
        List<User> users = fileHandler.loadUsers();
        users.removeIf(u -> u.getUserId().equals(id));
        fileHandler.saveUsers(users);
    }

    // Logout: invalidate session
    public void logOutUser(HttpSession session) {
        session.invalidate();
    }

    public User findById(String id) {
        return fileHandler.loadUsers().stream()
                .filter(u -> u.getUserId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
