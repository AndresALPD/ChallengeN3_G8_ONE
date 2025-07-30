package com.andres.literalura.repository;

import com.andres.literalura.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Optional<Author> findByNameIgnoreCase(String name);

    /**
     * Autores vivos en un año dado:
     * birthYear <= :year AND (deathYear IS NULL OR deathYear >= :year)
     */
    @Query("""
           SELECT a FROM Author a
           WHERE (a.birthYear IS NULL OR a.birthYear <= :year)
             AND (a.deathYear IS NULL OR a.deathYear >= :year)
           """)
    List<Author> findAliveInYear(int year);
}
