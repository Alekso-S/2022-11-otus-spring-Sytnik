package ru.otus.spring.dto;

import lombok.Data;
import ru.otus.spring.model.jpa.JpaComment;
import ru.otus.spring.model.mongo.MongoComment;

@Data
public class CommentDto {
    private String text;
    private String bookName;

    public CommentDto(MongoComment mongoComment) {
        this.text = mongoComment.getText();
        this.bookName = mongoComment.getBook().getName();
    }

    public CommentDto(JpaComment jpaComment) {
        this.text = jpaComment.getText();
        this.bookName = jpaComment.getBook().getName();
    }
}
