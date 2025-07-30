package com.andres.literalura.domain;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "books",
        indexes = {
                @Index(name = "idx_book_title", columnList = "title"),
                @Index(name = "idx_book_gutendex_id", columnList = "gutendex_id", unique = true)
        })
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID de Gutendex (campo "id" del BookDTO). Lo marcamos único para evitar duplicados.
     */
    @Column(name = "gutendex_id", unique = true)
    private Integer gutendexId;

    @Column(nullable = false)
    private String title;

    @Column(name = "download_count")
    private Integer downloadCount;

    /**
     * Idiomas tal como vienen en Gutendex (ej.: "en", "es").
     * Se guarda en una tabla secundaria: book_languages(book_id, language)
     */
    @ElementCollection
    @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "language")
    private List<String> languages = new ArrayList<>();

    /**
     * Relación muchos-a-muchos con autores.
     * Tabla intermedia: book_authors(book_id, author_id)
     */
    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new LinkedHashSet<>();

    // ======== Constructores ========
    protected Book() { /* JPA */ }

    public Book(Integer gutendexId, String title, Integer downloadCount, List<String> languages) {
        this.gutendexId = gutendexId;
        this.title = title;
        this.downloadCount = downloadCount;
        if (languages != null) this.languages.addAll(languages);
    }

    // ======== Helpers de dominio ========
    public void addAuthor(Author author) {
        if (author == null) return;
        this.authors.add(author);
        author.getBooks().add(this);
    }

    public void removeAuthor(Author author) {
        if (author == null) return;
        this.authors.remove(author);
        author.getBooks().remove(this);
    }

    // ======== Getters/Setters ========
    public Long getId() { return id; }
    public Integer getGutendexId() { return gutendexId; }
    public void setGutendexId(Integer gutendexId) { this.gutendexId = gutendexId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }
    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) {
        this.languages.clear();
        if (languages != null) this.languages.addAll(languages);
    }
    public Set<Author> getAuthors() { return authors; }
    public void setAuthors(Set<Author> authors) { this.authors = authors != null ? authors : new LinkedHashSet<>(); }

    // ======== equals/hashCode por id ========
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book b)) return false;
        return id != null && id.equals(b.id);
    }
    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
