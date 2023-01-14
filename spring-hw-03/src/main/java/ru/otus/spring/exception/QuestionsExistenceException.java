package ru.otus.spring.exception;

/**
 * Ошибка загрузки вопросов
 */
public class QuestionsExistenceException extends RuntimeException {
    public QuestionsExistenceException(String message) {
        super(message);
    }
}
