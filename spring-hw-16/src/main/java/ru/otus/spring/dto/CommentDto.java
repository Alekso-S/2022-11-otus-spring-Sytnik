package ru.otus.spring.dto;

import lombok.Data;
import ru.otus.spring.domain.Comment;

@Data
public class CommentDto {

    private final String id;
    private final String text;
    private String bookName;
    private String userName;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        if (comment.getUser() != null) {
            this.userName = comment.getUser().getUsername();
        }
    }
}
