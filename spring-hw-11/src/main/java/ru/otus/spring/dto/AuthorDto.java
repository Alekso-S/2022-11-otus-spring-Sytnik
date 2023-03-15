package ru.otus.spring.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.domain.Author;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class AuthorDto {

    private String name;

    public AuthorDto(Author author) {
        this.name = author.getName();
    }
}
