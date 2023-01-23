package ru.otus.spring.exception;

public class AuthorNotFoundEx extends ObjectNotFoundEx {

    public AuthorNotFoundEx(String message) {
        super(message);
    }
}
