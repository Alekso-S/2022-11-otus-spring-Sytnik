package ru.otus.spring.exception;

public class GenreNotFoundEx extends ObjectNotFoundEx {

    public GenreNotFoundEx(String message) {
        super(message);
    }

    public GenreNotFoundEx() {
        super();
    }
}
