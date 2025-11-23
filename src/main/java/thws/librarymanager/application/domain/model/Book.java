package thws.librarymanager.application.domain.model;

public class Book {
    private Long isbn;
    private String title;
    private String author;
    private String genre;
    private Library library;
    private Loan currentLoan;

    public Book(Long isbn, String title, String author, String genre, Library library, Loan currentLoan) {
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

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }

    public Loan getCurrentLoan() {
        return currentLoan;
    }

    public void setCurrentLoan(Loan currentLoan) {
        this.currentLoan = currentLoan;
    }

   public boolean isOnLoan() {
        return  false;
        //currentLoan != null && currentLoan.getStatus().equals("ACTIVE");

    }


}
