package ru.otus.spring.exceptions;

/**
 * Ошибка чтения данных источника
 */
public class DataReadException extends Exception {
    public DataReadException(Exception e) {
        super(e);
    }
}
