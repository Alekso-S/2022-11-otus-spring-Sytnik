package ru.otus.spring.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.otus.spring.dto.CommentDto;

import java.util.Objects;

@Data
@NoArgsConstructor
@Document("comments")
public class Comment {

    @Id
    private String id;

    private Book book;

    private String text;

    public Comment(Book book, String text) {
        this.book = book;
        this.text = text;
    }

    public Comment(String name) {
        this.text = name;
    }

    public Comment(String id, Book book, String text) {
        this.id = id;
        this.book = book;
        this.text = text;
    }

    public CommentDto toDto() {
        return new CommentDto(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() && getClass() != o.getClass().getSuperclass()) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
