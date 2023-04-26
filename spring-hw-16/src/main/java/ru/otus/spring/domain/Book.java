package ru.otus.spring.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.spring.dto.BookDto;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Document("books")
public class Book {

    @Id
    private String id;

    private String name;

    private Author author;

    private List<Genre> genres;

    public Book(String name, Author author, List<Genre> genres) {
        this.name = name;
        this.author = author;
        this.genres = genres;
    }

    public Book(String id, String name, Author author, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genres = genres;
    }

    public Book(String name, String authorName, String genreNames) {
        this.name = name;
        this.author = new Author(authorName);
        this.genres = Arrays.stream(genreNames.split(";"))
                .map(Genre::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() && getClass() != o.getClass().getSuperclass()) return false;
        Book book = (Book) o;
        return id.equals(book.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public BookDto toDto() {
        return new BookDto(this);
    }
}
