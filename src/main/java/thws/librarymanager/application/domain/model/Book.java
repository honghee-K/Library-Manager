package thws.librarymanager.application.domain.model;

public class Book {
    private Long isbn;
    private String title;
    private String author;
    private String genre;
    private Library library;
    private Loan currentLoan;

    public Book(Long isbn, String title, String author, String genre, Library library, Loan currentLoan) {
        if (isbn == null) throw new IllegalArgumentException("ISBN cannot be null.");
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.library = library;
        this.currentLoan = currentLoan;
    }

    public Long getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public Library getLibrary() {
        return library;
    }

    public Loan getCurrentLoan() {
        return currentLoan;
    }

    public void updateBook(String title, String author, String genre) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (author != null && !author.isBlank()) {
            this.author = author;
        }
        if (genre != null && !genre.isBlank()) {
            this.genre = genre;
        }
    }

    public void startLoan(Loan newLoan) {
        if (this.currentLoan != null) {
            throw new IllegalStateException("Book is already on loan.");
        }
        this.currentLoan = newLoan; //activate
    }

    public void endLoan() {
        if (this.currentLoan == null) {
            throw new IllegalStateException("Book is not currently on loan.");
        }
        this.currentLoan = null; //deactivate
    }

    public boolean isOnLoan() {
        return this.currentLoan != null;
    }


}
