package thws.librarymanager.application.domain.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String name;
    private String email;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("ID is already set and cannot be changed.");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank() || !isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        this.email = email;
    }


    public void updateInfo(String newName, String newEmail) {
        if (newName != null && !newName.isBlank()) {
            this.name = newName;
        }
        if (newEmail != null && !newEmail.isBlank() && isValidEmail(newEmail)) {
            this.email = newEmail;
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}