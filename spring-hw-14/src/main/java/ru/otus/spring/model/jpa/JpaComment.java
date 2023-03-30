package ru.otus.spring.model.jpa;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
public class JpaComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "book_id")
    private JpaBook book;

    private String text;

    public JpaComment(String text, JpaBook book) {
        this.text = text;
        this.book = book;
    }
}
