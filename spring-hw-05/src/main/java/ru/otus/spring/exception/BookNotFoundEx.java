package ru.otus.spring.exception;

public class BookNotFoundEx extends ObjectNotFoundEx {

    public BookNotFoundEx(String message) {
        super(message);
    }
}
