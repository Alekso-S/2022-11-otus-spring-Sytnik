package ru.otus.spring.exception;

public class CommentNotFoundEx extends ObjectNotFoundEx {

    public CommentNotFoundEx() {
        super();
    }

    public CommentNotFoundEx(String commentId) {
        super(String.format("Comment with id '%s' was not found", commentId));
    }
}
