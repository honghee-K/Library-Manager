package thws.librarymanager.application.domain.models;

import java.time.LocalDate;

public class Loan {

    private Long id;
    private final User user;
    private final Book book;
    private final LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status; // ACTIVE | RETURNED

    private Loan(
            Long id,
            User user,
            Book book,
            LocalDate loanDate,
            LocalDate dueDate,
            LocalDate returnDate,
            LoanStatus status) {

        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        if (book == null) {
            throw new IllegalArgumentException("book must not be null");
        }

        if (loanDate == null) {
            throw new IllegalArgumentException("loanDate must not be null");
        }

        if (dueDate == null || dueDate.isBefore(loanDate)) {
            throw new IllegalArgumentException("dueDate must be >= loanDate");
        }

        if (status == LoanStatus.RETURNED && returnDate == null) {
            throw new IllegalStateException("Returned loan must have returnDate");
        }

        if (status == LoanStatus.ACTIVE && returnDate != null) {
            throw new IllegalStateException("Active loan cannot have returnDate");
        }

        this.id = id;
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public static Loan restore(
            Long id,
            User user,
            Book book,
            LocalDate loanDate,
            LocalDate dueDate,
            LocalDate returnDate,
            LoanStatus status) {
        return new Loan(id, user, book, loanDate, dueDate, returnDate, status);
    }

    public static Loan createLoan(User user, Book book, LocalDate loanDate, LocalDate dueDate) {
        return new Loan(null, user, book, loanDate, dueDate, null, LoanStatus.ACTIVE);
    }

    public void setReturned(LocalDate returnDate) {
        if (this.status == LoanStatus.RETURNED) {
            throw new IllegalStateException("Loan already returned");
        }

        if (returnDate == null) {
            this.returnDate = LocalDate.now();
        } else {
            this.returnDate = returnDate;
        }

        this.status = LoanStatus.RETURNED;
    }

    public void changeDueDate(LocalDate newDueDate) {
        if (newDueDate == null || newDueDate.isBefore(this.loanDate)) {
            throw new IllegalArgumentException("New dueDate must be >= loanDate");
        }
        if (this.status == LoanStatus.RETURNED) {
            throw new IllegalStateException("Returned loan cannot change dueDate");
        }
        this.dueDate = newDueDate;
    }

    public boolean isActive() {
        return status == LoanStatus.ACTIVE;
    }

    public boolean isReturned() {
        return status == LoanStatus.RETURNED;
    }

    public boolean isOverdue(LocalDate today) {
        if (!isActive()) return false;
        return today.isAfter(dueDate);
    }

    public Long getId() {
        return id;
    }

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

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setLoanId(Long id) {
        if (this.id != null) {
            throw new IllegalStateException("ID already set");
        }
        this.id = id;
    }
}
