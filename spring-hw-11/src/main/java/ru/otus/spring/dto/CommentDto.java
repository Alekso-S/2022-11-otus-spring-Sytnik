package ru.otus.spring.dto;

import lombok.Data;
import ru.otus.spring.domain.Comment;

@Data
public class CommentDto {

    private final String id;
    private final String text;
    private String bookName;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
    }
}
