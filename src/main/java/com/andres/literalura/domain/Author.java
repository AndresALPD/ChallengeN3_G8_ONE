package com.andres.literalura.domain;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "authors",
        indexes = {
                @Index(name = "idx_author_name", columnList = "name", unique = true)
        })
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Muchos autores en Gutendex solo tienen nombre; lo marcamos único para evitar duplicados básicos.
     * (Puedes relajar esto si necesitas permitir homónimos).
     */
    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "death_year")
    private Integer deathYear;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new LinkedHashSet<>();

    // ======== Constructores ========
    protected Author() { /* JPA */ }

    public Author(String name, Integer birthYear, Integer deathYear) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
    }

    // ======== Getters/Setters ========
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getBirthYear() { return birthYear; }
    public void setBirthYear(Integer birthYear) { this.birthYear = birthYear; }
    public Integer getDeathYear() { return deathYear; }
    public void setDeathYear(Integer deathYear) { this.deathYear = deathYear; }
    public Set<Book> getBooks() { return books; }
    public void setBooks(Set<Book> books) { this.books = books != null ? books : new LinkedHashSet<>(); }

    // ======== equals/hashCode por id ========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author a)) return false;
        return id != null && id.equals(a.id);
    }
    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
