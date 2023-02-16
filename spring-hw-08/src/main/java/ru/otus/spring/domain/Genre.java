package ru.otus.spring.domain;

import lombok.Data;

import java.util.Objects;

@Data
public class Genre {

    private String name;

    public Genre(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() && getClass() != o.getClass().getSuperclass()) return false;
        Genre genre = (Genre) o;
        return name.equals(genre.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
