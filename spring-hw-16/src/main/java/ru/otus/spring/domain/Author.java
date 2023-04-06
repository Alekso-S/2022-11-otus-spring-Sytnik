package ru.otus.spring.domain;

import lombok.Data;
import ru.otus.spring.dto.AuthorDto;

import java.util.Objects;

@Data
public class Author {

    private String name;

    public Author(String name) {
        this.name = name;
    }

    public AuthorDto toDto() {
        return new AuthorDto(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() && getClass() != o.getClass().getSuperclass()) return false;
        Author author = (Author) o;
        return name.equals(author.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
