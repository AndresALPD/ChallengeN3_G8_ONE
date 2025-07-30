package com.andres.literalura.dto;

import java.util.List;

public class BookDTO {
    private Integer id;
    private String title;
    private List<AuthorDTO> authors;
    private List<String> languages;
    private Integer download_count;

    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public List<AuthorDTO> getAuthors() { return authors; }
    public List<String> getLanguages() { return languages; }
    public Integer getDownload_count() { return download_count; }

    public void setId(Integer id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthors(List<AuthorDTO> authors) { this.authors = authors; }
    public void setLanguages(List<String> languages) { this.languages = languages; }
    public void setDownload_count(Integer download_count) { this.download_count = download_count; }
}
