package thws.librarymanager.application.domain.model;

import java.time.LocalDate;

public class Loan {

    private final User user;
    private final Book book;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private LoanStatus status;

    public Loan(User user, Book book, int loanDurationDays) {
        this.user = user;
        this.book = book;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(loanDurationDays);
        this.status = LoanStatus.ACTIVE;
    }

    // ✅ DOMAIN: Kitap iade işlemi
    public void returnBook() {
        this.status = LoanStatus.RETURNED;
        this.book.clearLoan();  // Book artık boş
    }

    // ✅ DOMAIN: Gecikme kontrolü
    public boolean isOverdue(LocalDate today) {
        return status == LoanStatus.ACTIVE && dueDate.isBefore(today);
    }

    // ✅ GETTER’lar
    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LoanStatus getStatus() {
        return status;
    }
}
