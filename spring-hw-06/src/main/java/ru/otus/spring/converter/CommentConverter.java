package ru.otus.spring.converter;

import ru.otus.spring.domain.Comment;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

    public static String toString(Comment comment) {
        return "id=" + comment.getId() +
                ", text='" + comment.getText() + '\'';
    }

    public static String toString(List<Comment> comments) {
        return comments.stream()
                .sorted(Comparator.comparing(Comment::getId))
                .map(CommentConverter::toString)
                .collect(Collectors.joining("\n"));
    }
}
