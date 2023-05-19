package ru.otus.spring.exception;

public class BookNotFoundEx extends ObjectNotFoundEx {

    public BookNotFoundEx(String paramType, String paramValue) {
        super(String.format("Book with %s '%s' was not found", paramType, paramValue));
    }
}
