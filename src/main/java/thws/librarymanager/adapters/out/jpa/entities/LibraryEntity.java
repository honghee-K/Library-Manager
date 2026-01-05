package thws.librarymanager.adapters.out.jpa.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "library")
public class LibraryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String location;

    // @OneToMany(mappedBy = "library", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // private List<BookEntity> books = new ArrayList<>();

    public LibraryEntity() {}

    public LibraryEntity(String name, String location) {
        this.name = name;
        this.location = location;
    }

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /*public List<BookEntity> getBooks() { return books; }
    public void setBooks(List<BookEntity> books) { this.books = books; }

    // Yardımcı metodlar
    public void addBook(BookEntity book) {
        books.add(book);
       // book.setLibrary(this);
    }

    public void removeBook(BookEntity book) {
        books.remove(book);
       // book.setLibrary(null);
    }*/
}
