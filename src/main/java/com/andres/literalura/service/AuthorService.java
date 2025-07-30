package com.andres.literalura.service;

import com.andres.literalura.domain.Author;
import com.andres.literalura.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Author> findAliveInYear(int year) {
        return authorRepository.findAliveInYear(year);
    }

    @Transactional
    public Author getOrCreate(String name, Integer birthYear, Integer deathYear) {
        if (name == null || name.isBlank()) {
            name = "(Autor desconocido)";
        }
        String finalName = name;
        return authorRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> authorRepository.save(new Author(finalName, birthYear, deathYear)));
    }
}
