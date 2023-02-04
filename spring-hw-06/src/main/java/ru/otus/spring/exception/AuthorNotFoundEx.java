package ru.otus.spring.exception;

public class AuthorNotFoundEx extends ObjectNotFoundEx {

    public AuthorNotFoundEx() {
        super();
    }

    public AuthorNotFoundEx(Exception e) {
        super(e);
    }
}
