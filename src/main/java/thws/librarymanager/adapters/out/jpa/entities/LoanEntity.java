package thws.librarymanager.adapters.out.jpa.entities;

import java.time.LocalDate;

import jakarta.persistence.*;

import thws.librarymanager.adapters.out.jpa.enums.LoanStatusJpa;
import thws.librarymanager.application.domain.models.LoanStatus;

@Entity
@Table(name = "loan")
public class LoanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    // Kullanıcı ilişkisi
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // Kitap ilişkisi
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "isbn", referencedColumnName = "isbn", nullable = false)
    private BookEntity book;

    // Tarihler
    @Column(name = "loan_date", nullable = false)
    private LocalDate loanDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LoanStatusJpa status;

    public LoanEntity() {}

    public LoanEntity(UserEntity user, BookEntity book, LocalDate loanDate, LocalDate dueDate, LoanStatusJpa status) {
        this.user = user;
        this.book = book;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BookEntity getBook() { return book; }
    public void setBook(BookEntity book) { this.book = book; }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatusJpa getStatus() {
        return status;
    }

    public void setStatus(LoanStatusJpa status) {
        this.status = status;
    }

    // ---- YARDIMCI METOTLAR (optional ama çok faydalı) ----
    public boolean isReturned() {
        return this.status == LoanStatusJpa.RETURNED;
    }

    public boolean isOverdue(LocalDate today) {
        return !isReturned() && today.isAfter(dueDate);
    }

    public void markAsReturned(LocalDate returnDate) {
        this.returnDate = returnDate != null ? returnDate : LocalDate.now();
        this.status = LoanStatusJpa.RETURNED;
    }
}
