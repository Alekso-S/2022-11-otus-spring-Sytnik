package ru.otus.spring.exception;

/**
 * Ошибка парсинга данных источника
 */
public class DataValidityException extends Exception {
    public DataValidityException(String s) {
        super(s);
    }
}
