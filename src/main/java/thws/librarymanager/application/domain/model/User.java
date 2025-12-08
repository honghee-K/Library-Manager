package thws.librarymanager.application.domain.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Long id;
    private String name;
    private final List<Loan> loans = new ArrayList<>();

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // ✅ DOMAIN: Loan başlatma
    public Loan startLoan(Book book, int days) {
        Loan loan = new Loan(this, book, days);
        book.setCurrentLoan(loan);
        loans.add(loan);
        return loan;
    }

    // ✅ DOMAIN: Loan bitirme
    public void endLoan(Loan loan) {
        loan.returnBook();
    }

    public Long getId() {
        return id;
    }

    public List<Loan> getLoans() {
        return loans;
    }
}