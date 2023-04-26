package ru.otus.spring.exception;

public class BookAlreadyExistsEx extends Exception {

    public BookAlreadyExistsEx(String bookName) {
        super(String.format("Book with name '%s' already exists", bookName));
    }
}
