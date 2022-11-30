package ru.otus.spring.domain;

/**
 * Вариант ответа на вопрос.
 * Содержит текстовку и валидность.
 */
public class Answer {

    private final String text;
    private final boolean validity;

    public Answer(String text, boolean validity) {
        this.text = text;
        this.validity = validity;
    }

    public String getText() {
        return text;
    }

    public boolean isValidity() {
        return validity;
    }

}
