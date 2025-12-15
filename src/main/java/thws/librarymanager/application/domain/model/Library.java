package thws.librarymanager.application.domain.model;

import java.util.List;
import java.util.stream.Collectors;

public class Library {
    private Long id;
    private String name;
    private String location;
    private List<Book> books; // List of Book objects associated with this Library

    // Constructor used by persistence layer to reconstruct the object
    public Library(Long id, String name, String location, List<Book> books) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Library name must not be null or blank");
        }
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Library location must not be null or blank");
        }

        this.id = id;
        this.name = name;
        this.location = location;
        this.books = books;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public List<Book> getBooks() { return books; } // Returns the list of book objects

    // --- Domain Behavior (Business Logic) ---

    // UML Method: Adds a book to the library's collection
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null.");
        }
        // Business rule: Ensure the book is not already in this list (optional, but safer)
        if (this.books.contains(book)) {
            throw new IllegalStateException("Book is already registered in this library.");
        }
        this.books.add(book);
    }

    // UML Method: Removes a book from the library's collection
    public void removeBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book must not be null.");
        }
        // Business rule: The book must be in the library
        if (!this.books.contains(book)) {
            throw new IllegalStateException("Book is not registered in this library.");
        }

        // Business rule: Cannot remove a book that is currently on loan (delegated check)
        if (book.isOnLoan()) {
            throw new IllegalStateException("Cannot remove a book that is currently on loan.");
        }

        this.books.remove(book);
    }

    // UML Method: Searches books by genre
    public List<Book> getBooksByGenre(String genre) {
        if (genre == null || genre.isBlank()) {
            throw new IllegalArgumentException("Genre must not be null or blank.");
        }
        return this.books.stream()
                .filter(book -> book.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }

    // UML Method: Searches books by author
    public List<Book> getBooksByAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author must not be null or blank.");
        }
        return this.books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .collect(Collectors.toList());
    }
}