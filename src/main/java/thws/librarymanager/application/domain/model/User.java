package thws.librarymanager.application.domain.model;
import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String name;
    private String email;

    // Constructor
    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    // Business Logic: Update user information
    public void updateInfo(String newName, String newEmail) {
        if (newName != null && !newName.isBlank()) {
            this.name = newName;
        }
        if (newEmail != null && !newEmail.isBlank() && isValidEmail(newEmail)) {
            this.email = newEmail;
        }
    }

    // Validation logic
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}


