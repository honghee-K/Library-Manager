package thws.librarymanager.adapters.in.rest.models;

import java.util.Map;

public class StatisticsDTO {

    private BooksStatistics books;
    private UsersStatistics users;
    private LoansStatistics loans;

    public BooksStatistics getBooks() {
        return books;
    }

    public void setBooks(BooksStatistics books) {
        this.books = books;
    }

    public UsersStatistics getUsers() {
        return users;
    }

    public void setUsers(UsersStatistics users) {
        this.users = users;
    }

    public LoansStatistics getLoans() {
        return loans;
    }

    public void setLoans(LoansStatistics loans) {
        this.loans = loans;
    }

    public static class BooksStatistics {
        private long total;
        private Map<String, Long> booksByGenre;
        private Map<String, Long> booksByAuthor;

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }

        public Map<String, Long> getBooksByGenre() {
            return booksByGenre;
        }

        public void setBooksByGenre(Map<String, Long> booksByGenre) {
            this.booksByGenre = booksByGenre;
        }

        public Map<String, Long> getBooksByAuthor() {
            return booksByAuthor;
        }

        public void setBooksByAuthor(Map<String, Long> booksByAuthor) {
            this.booksByAuthor = booksByAuthor;
        }
    }

    public static class UsersStatistics {
        private long registered;

        public long getRegistered() {
            return registered;
        }

        public void setRegistered(long registered) {
            this.registered = registered;
        }
    }

    public static class LoansStatistics {
        private long active;

        public long getActive() {
            return active;
        }

        public void setActive(long active) {
            this.active = active;
        }
    }
}
