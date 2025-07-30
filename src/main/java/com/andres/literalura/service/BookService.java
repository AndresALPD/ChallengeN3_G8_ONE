package com.andres.literalura.service;

import com.andres.literalura.domain.Author;
import com.andres.literalura.domain.Book;
import com.andres.literalura.dto.AuthorDTO;
import com.andres.literalura.dto.BookDTO;
import com.andres.literalura.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Book> findAllDetailed() {
        return bookRepository.findAllWithLanguagesAndAuthors();
    }

    @Transactional(readOnly = true)
    public List<Book> findByLanguageDetailed(String language) {
        return bookRepository.findByLanguageWithAuthors(language);
    }

    @Transactional(readOnly = true)
    public List<Book> top10ByDownloadsDetailed() {
        var list = bookRepository.findTop10WithAuthorsOrderByDownloadCountDesc();
        return list.size() > 10 ? list.subList(0, 10) : list;
    }


    /**
     * Mapea un BookDTO de Gutendex a entidad y guarda evitando duplicados por gutendexId.
     * Retorna la entidad persistida (existente o nueva).
     */
    @Transactional
    public Book saveFromDto(BookDTO dto) {
        if (dto == null) throw new IllegalArgumentException("BookDTO nulo");

        // Evitar duplicados por gutendexId
        var existing = bookRepository.findByGutendexId(dto.getId());
        if (existing.isPresent()) {
            // Actualización mínima opcional por si cambió download_count o languages
            Book book = existing.get();
            book.setDownloadCount(dto.getDownload_count());
            if (dto.getLanguages() != null && !dto.getLanguages().isEmpty()) {
                book.setLanguages(dto.getLanguages());
            }
            // Actualizar autores por si hay nuevos
            mergeAuthorsFromDto(book, dto);
            return bookRepository.save(book);
        }

        // Crear nuevo libro
        Book book = new Book(
                dto.getId(),
                dto.getTitle(),
                dto.getDownload_count(),
                dto.getLanguages()
        );

        // Autores
        mergeAuthorsFromDto(book, dto);

        return bookRepository.save(book);
    }

    private void mergeAuthorsFromDto(Book book, BookDTO dto) {
        Set<Author> authors = new LinkedHashSet<>();
        if (dto.getAuthors() != null) {
            for (AuthorDTO a : dto.getAuthors()) {
                Author author = authorService.getOrCreate(a.getName(), a.getBirth_year(), a.getDeath_year());
                authors.add(author);
            }
        }
        // Sincronizar relación bidireccional usando helpers
        // Limpiar actuales y volver a asignar
        book.getAuthors().forEach(current -> current.getBooks().remove(book));
        book.getAuthors().clear();
        for (Author a : authors) {
            book.addAuthor(a);
        }
    }
}
