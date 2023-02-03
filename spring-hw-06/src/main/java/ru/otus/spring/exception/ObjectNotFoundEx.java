package ru.otus.spring.exception;

public abstract class ObjectNotFoundEx extends Exception {

    public ObjectNotFoundEx(String message) {
        super(message);
    }

    public ObjectNotFoundEx() {
        super();
    }
}
