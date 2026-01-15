package thws.librarymanager.application.domain.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Long id;
    private String name;
    private String email;

    private List<Loan> loans;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.loans = new ArrayList<>();
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

    public List<Loan> getLoans() {
        return loans != null ? loans : new ArrayList<>();
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


    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }


    public void updateInfo(String newName, String newEmail) {
        if (newName != null && !newName.isBlank()) {
            this.name = newName;
        }
        if (newEmail != null && !newEmail.isBlank() && isValidEmail(newEmail)) {
            this.email = newEmail;
        }
    }

    public void addLoan(Loan loan) {
        if (this.loans == null) {
            this.loans = new ArrayList<>();
        }
        if (!this.loans.contains(loan)) {
            this.loans.add(loan);
        }
    }

    public void deleteLoan(Loan loan) {//TODO: Ohne Loan History?
        if (this.loans != null) {
            this.loans.remove(loan);
        }
        // loan.setReturned(LocalDate.now());
    }

    public boolean hasActiveLoans() {
        return loans.stream().anyMatch(Loan::isActive);
    }

}
