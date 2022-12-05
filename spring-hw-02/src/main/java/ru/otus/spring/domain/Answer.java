package ru.otus.spring.domain;

/**
 * Вариант ответа на вопрос.
 * Содержит текстовку и валидность.
 */
public class Answer {

    private final String text;
    private final boolean valid;

    public Answer(String text, boolean valid) {
        this.text = text;
        this.valid = valid;
    }

    public String getText() {
        return text;
    }

    public boolean isValid() {
        return valid;
    }

}
