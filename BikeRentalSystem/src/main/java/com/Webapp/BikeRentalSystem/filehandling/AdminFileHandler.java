package com.Webapp.BikeRentalSystem.filehandling;

import com.Webapp.BikeRentalSystem.model.Admin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin accounts are kept in a plain text file: <app.data-dir>/admins.txt
 * One admin per line:  email|password   (lines starting with # are comments).
 * The file is created with one default admin the first time the app starts.
 */
@Component
public class AdminFileHandler {

    private final FileHandler fileHandler;
    private final String path;

    public AdminFileHandler(FileHandler fileHandler,
                            @Value("${app.data-dir}") String dataDir) {
        this.fileHandler = fileHandler;
        this.path = dataDir + "/admins.txt";
        createDefaultFileIfMissing();
    }

    public String getPath() {
        return path;
    }

    public List<Admin> loadAdmins() {
        List<Admin> admins = new ArrayList<>();
        for (String line : fileHandler.readLines(path)) {
            String t = line.trim();
            if (t.isEmpty() || t.startsWith("#")) continue;
            String[] p = t.split("\\|", 2);
            if (p.length < 2) continue;
            admins.add(new Admin(p[0].trim(), p[1].trim()));
        }
        return admins;
    }

    private void createDefaultFileIfMissing() {
        if (Files.exists(Paths.get(path))) return;
        fileHandler.writeLines(path, List.of(
                "# RIDEX admin accounts - one per line:  email|password",
                "# Add or edit admins here. They sign in on the normal sign-in page.",
                "admin@ridex.com|admin123"
        ));
    }
}
