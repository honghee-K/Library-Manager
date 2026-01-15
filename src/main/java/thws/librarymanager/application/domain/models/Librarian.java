package thws.librarymanager.application.domain.models;

public class Librarian {
    private Long id;
    private String name;

    public Librarian(Long id, String name) {
        this.id = id;
        this.setName(name);
    }

    public Long getId() { return id; }
    public String getName() { return name; }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Librarian name cannot be empty.");
        }
        this.name = name;
    }
}