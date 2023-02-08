package ru.otus.spring.exception;

public abstract class ObjectNotFoundEx extends Exception {

    public ObjectNotFoundEx() {
        super();
    }

    public ObjectNotFoundEx(Exception e) {
        super(e);
    }
}
