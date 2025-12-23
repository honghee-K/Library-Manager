package thws.librarymanager.application.domain.model;

public class Book {
    private Long isbn;
    private String title;
    private String author;
    private String genre;
    private Long libraryId; // don't reuse classes in different components (4) , to allow loanId to be null
    private Long loanId; // don't reuse classes in different components (4), to allow libraryId to be null

    public Book(Long isbn, String title, String author, String genre, Long libraryId, Long loanId) {
        if (isbn == null) throw new IllegalArgumentException("ISBN cannot be null.");
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.libraryId = libraryId;
        this.loanId = loanId;
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

    public Long getLibraryId() {
        return libraryId;
    }

    public Long getLoanId() {
        return loanId;
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

    public void startLoan(Long newLoanId) {
        if (this.loanId != null) {
            throw new IllegalStateException("Book is already on loan.");
        }
        this.loanId = newLoanId; //activate
    }

    public void endLoan() {
        if (this.loanId == null) {
            throw new IllegalStateException("Book is not currently on loan.");
        }
        this.loanId = null; //deactivate
    }

    public boolean isOnLoan() {
        return this.loanId != null;
    }


}
