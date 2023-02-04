package ru.otus.spring.exception;

public class GenreNotFoundEx extends ObjectNotFoundEx {

    public GenreNotFoundEx() {
        super();
    }

    public GenreNotFoundEx(Exception e) {
        super(e);
    }
}
