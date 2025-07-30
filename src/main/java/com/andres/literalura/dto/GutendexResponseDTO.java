package com.andres.literalura.dto;

import java.util.List;

public class GutendexResponseDTO {
    private Integer count;
    private String next;
    private String previous;
    private List<BookDTO> results;

    public Integer getCount() { return count; }
    public String getNext() { return next; }
    public String getPrevious() { return previous; }
    public List<BookDTO> getResults() { return results; }

    public void setCount(Integer count) { this.count = count; }
    public void setNext(String next) { this.next = next; }
    public void setPrevious(String previous) { this.previous = previous; }
    public void setResults(List<BookDTO> results) { this.results = results; }
}
