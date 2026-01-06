package thws.librarymanager.adapters.out.jpa.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long isbn;

    private String title;
    private String author;
    private String genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id", nullable = false)
    private LibraryEntity library;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_loan_id", referencedColumnName = "id")
    private LoanEntity currentLoan;

    public BookEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LibraryEntity getLibrary() {
        return library;
    }

    public void setLibrary(LibraryEntity library) {
        this.library = library;
    }

    public LoanEntity getCurrentLoan() {
        return currentLoan;
    }

    public void setCurrentLoan(LoanEntity currentLoan) {
        this.currentLoan = currentLoan;
    }
}
