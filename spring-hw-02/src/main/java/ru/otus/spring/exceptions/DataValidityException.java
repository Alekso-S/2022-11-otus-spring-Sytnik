package ru.otus.spring.exceptions;

/**
 * Ошибка парсинга данных источника
 */
public class DataValidityException extends RuntimeException {
    public DataValidityException(String message) {
        super(message);
    }
}
