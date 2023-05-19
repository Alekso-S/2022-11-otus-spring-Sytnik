package ru.otus.spring.dto;

import lombok.Data;
import ru.otus.spring.domain.Author;

@Data
public class AuthorDto {

    private final String name;

    public AuthorDto(Author author) {
        this.name = author.getName();
    }
}
