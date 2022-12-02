package ru.otus.spring.domain;

/**
 * Вопрос.
 * Содержит только текстовку.
 */
public class Question {
    private final String text;

    public Question(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
