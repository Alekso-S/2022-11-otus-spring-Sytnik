package ru.otus.spring.exception;

/**
 * Ошибка чтения данных источника
 */
public class DataReadException extends Exception {
    public DataReadException(Exception e) {
        super(e);
    }
}
