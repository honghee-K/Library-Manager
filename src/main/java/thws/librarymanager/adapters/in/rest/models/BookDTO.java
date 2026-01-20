package thws.librarymanager.adapters.in.rest.models;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class BookDTO extends BaseDTO {
    private Long isbn;
    private String title;
    private String author;
    private String genre;
    private Long libraryId;

    @Schema(readOnly = true)
    private boolean OnLoan = false;

    public BookDTO() {
        super();
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

    public Long getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(Long libraryId) {
        this.libraryId = libraryId;
    }

    public boolean isOnLoan() {
        return OnLoan;
    }

    public void setOnLoan(boolean onLoan) {
        OnLoan = onLoan;
    }
}
