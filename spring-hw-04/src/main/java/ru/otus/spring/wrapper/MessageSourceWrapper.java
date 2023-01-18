package ru.otus.spring.wrapper;

public interface MessageSourceWrapper {

    String getMessage(String code, Object... args);
}
