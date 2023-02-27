package ru.otus.spring.exception;

public class BookNotFoundEx extends ObjectNotFoundEx {

    public BookNotFoundEx(String bookName) {
        super(String.format("Book with name %s was not found", bookName));
    }
}
