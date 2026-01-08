package thws.librarymanager.adapters.out.jpa.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "librarians")
public class LibrarianEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    public LibrarianEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}