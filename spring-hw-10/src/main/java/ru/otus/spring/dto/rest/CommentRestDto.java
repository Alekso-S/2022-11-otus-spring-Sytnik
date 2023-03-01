package ru.otus.spring.dto.rest;

import lombok.Data;

@Data
public class CommentRestDto {

    private final String id;
    private final String bookId;
    private final String text;
}
