package ru.otus.spring.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Вопрос.
 * Содержит текстовку и ответы.
 */
public class Question {

    private final String text;
    private final List<Answer> answers = new ArrayList<>();

    public Question(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

}
