package ru.otus.spring.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Вопрос.
 * Содержит текстовку и ответы.
 */
public class Question {

    private final String text;
    private final List<Answer> answers;

    public Question(String text) {
        this.text = text;
        this.answers = new ArrayList<>();
    }

    public Question(String text, List<Answer> answers) {
        this.text = text;
        this.answers = answers;
    }

    public String getText() {
        return text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return text.equals(question.text) && answers.equals(question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, answers);
    }

    @Override
    public String toString() {
        return "Question{" +
                "text='" + text + '\'' +
                ", answers=" + answers +
                '}';
    }
}
