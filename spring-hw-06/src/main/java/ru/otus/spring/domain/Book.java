package ru.otus.spring.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @ToString.Exclude
    private Author author;

    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @ToString.Exclude
    private List<Genre> genres;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    @ToString.Exclude
    private List<Comment> comments;

    public Book(String name, Author author, List<Genre> genres) {
        this.name = name;
        this.author = author;
        this.genres = genres;
    }

    public Book(Long id, String name, Author author, List<Genre> genres, List<Comment> comments) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.genres = genres;
        this.comments = comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass() && getClass() != o.getClass().getSuperclass()) return false;
        Book book = (Book) o;
        return id.equals(book.getId()) && name.equals(book.getName()) && author.equals(book.getAuthor())
                && new ArrayList<>(genres).equals(new ArrayList<>(book.getGenres()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, genres);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author=" + author +
                ", genres=" + genres +
                '}';
    }
}
