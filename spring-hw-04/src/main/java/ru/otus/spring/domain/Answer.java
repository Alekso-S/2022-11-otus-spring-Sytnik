package ru.otus.spring.domain;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return valid == answer.valid && text.equals(answer.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, valid);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "text='" + text + '\'' +
                ", valid=" + valid +
                '}';
    }
}
