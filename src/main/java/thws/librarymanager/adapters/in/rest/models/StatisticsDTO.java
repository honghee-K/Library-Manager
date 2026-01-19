package thws.librarymanager.adapters.in.rest.models;

public class StatisticsDTO extends BaseDTO {

    private Long totalBooks;
    private Long booksByGenre;
    private Long booksByAuthor;
    private Long activeLoans;
    private Long registeredUsers;

    public StatisticsDTO() {
        super();
    }

    public Long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(Long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public Long getBooksByGenre() {
        return booksByGenre;
    }

    public void setBooksByGenre(Long booksByGenre) {
        this.booksByGenre = booksByGenre;
    }

    public Long getBooksByAuthor() {
        return booksByAuthor;
    }

    public void setBooksByAuthor(Long booksByAuthor) {
        this.booksByAuthor = booksByAuthor;
    }

    public Long getActiveLoans() {
        return activeLoans;
    }

    public void setActiveLoans(Long activeLoans) {
        this.activeLoans = activeLoans;
    }

    public Long getRegisteredUsers() {
        return registeredUsers;
    }

    public void setRegisteredUsers(Long registeredUsers) {
        this.registeredUsers = registeredUsers;
    }
}
