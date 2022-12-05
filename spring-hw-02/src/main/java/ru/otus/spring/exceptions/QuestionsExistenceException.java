package ru.otus.spring.exceptions;

/**
 * Ошибка загрузки вопросов
 */
public class QuestionsExistenceException extends RuntimeException {
    public QuestionsExistenceException(String message) {
        super(message);
    }
}
