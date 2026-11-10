package com.Webapp.BikeRentalSystem.filehandling;

import com.Webapp.BikeRentalSystem.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserFileHandler {

    private final FileHandler fileHandler;
    private final String path;

    public UserFileHandler(FileHandler fileHandler,
                           @Value("${app.data-dir}") String dataDir) {
        this.fileHandler = fileHandler;
        this.path = dataDir + "/users.txt";
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        for (String line : fileHandler.readLines(path)) {
            users.add(User.fromFileString(line));
        }
        return users;
    }

    public void saveUsers(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u : users) lines.add(u.toFileString());
        fileHandler.writeLines(path, lines);
    }

    public String nextId(List<User> users) {
        return fileHandler.nextId("U",
                users.stream().map(User::getUserId).toList());
    }
}
