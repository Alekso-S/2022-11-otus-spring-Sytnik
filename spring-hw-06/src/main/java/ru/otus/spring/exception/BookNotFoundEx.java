package ru.otus.spring.exception;

public class BookNotFoundEx extends ObjectNotFoundEx {

    public BookNotFoundEx() {
        super();
    }

    public BookNotFoundEx(Exception e) {
        super(e);
    }
}
