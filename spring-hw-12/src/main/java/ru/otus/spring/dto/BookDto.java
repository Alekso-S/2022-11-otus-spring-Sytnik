package ru.otus.spring.dto;

import lombok.Data;
import ru.otus.spring.domain.Book;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BookDto {

    private String id;

    private String name;

    private AuthorDto author;

    private List<GenreDto> genres;

    public BookDto(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.author = book.getAuthor().toDto();
        this.genres = book.getGenres().stream()
                .map(GenreDto::new)
                .collect(Collectors.toList());
    }
}
