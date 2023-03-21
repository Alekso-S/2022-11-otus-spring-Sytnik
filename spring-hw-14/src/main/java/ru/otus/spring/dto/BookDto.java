package ru.otus.spring.dto;

import lombok.Data;
import ru.otus.spring.model.jpa.JpaBook;
import ru.otus.spring.model.jpa.JpaGenre;
import ru.otus.spring.model.mongo.MongoBook;
import ru.otus.spring.model.mongo.MongoGenre;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BookDto {
    private String name;
    private String authorName;
    private List<String> genreNames;

    public BookDto(MongoBook mongoBook) {
        this.name = mongoBook.getName();
        this.authorName = mongoBook.getAuthor().getName();
        this.genreNames = mongoBook.getGenres().stream()
                .map(MongoGenre::getName)
                .collect(Collectors.toList());
    }
    public BookDto(JpaBook jpaBook) {
        this.name = jpaBook.getName();
        this.authorName = jpaBook.getAuthor().getName();
        this.genreNames = jpaBook.getGenres().stream()
                .map(JpaGenre::getName)
                .collect(Collectors.toList());
    }
}
