package ru.otus.spring.exception;

public abstract class ObjectNotFoundEx extends Exception {

    public ObjectNotFoundEx() {
        super();
    }

    public ObjectNotFoundEx(String message) {
        super(message);
    }
}
