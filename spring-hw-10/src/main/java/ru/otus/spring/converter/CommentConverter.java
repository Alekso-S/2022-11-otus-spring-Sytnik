package ru.otus.spring.converter;

import ru.otus.spring.domain.Comment;
import ru.otus.spring.dto.CommentDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

    public static List<CommentDto> toDto(List<Comment> comments) {
        return comments.stream()
                .map(CommentDto::new)
                .sorted(Comparator.comparing(CommentDto::getId))
                .collect(Collectors.toList());
    }
}
