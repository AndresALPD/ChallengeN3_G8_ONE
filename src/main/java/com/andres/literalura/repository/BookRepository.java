package com.andres.literalura.repository;

import com.andres.literalura.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByGutendexId(Integer gutendexId);

    @Query("""
       SELECT DISTINCT b
       FROM Book b
       LEFT JOIN FETCH b.languages l
       LEFT JOIN FETCH b.authors a
       """)
    List<Book> findAllWithLanguagesAndAuthors();

    @Query("""
       SELECT DISTINCT b
       FROM Book b
       JOIN b.languages l
       LEFT JOIN FETCH b.authors a
       WHERE l = :language
       """)
    List<Book> findByLanguageWithAuthors(String language);

    @Query("""
       SELECT DISTINCT b
       FROM Book b
       LEFT JOIN FETCH b.authors a
       ORDER BY b.downloadCount DESC
       """)
    List<Book> findTop10WithAuthorsOrderByDownloadCountDesc();
}
