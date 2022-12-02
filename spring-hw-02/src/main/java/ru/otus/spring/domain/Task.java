package ru.otus.spring.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Задание.
 * Содержит вопрос и ответы к нему.
 */
public class Task {

    private final Question question;
    private final List<Answer> answers = new ArrayList<>();

    public Task(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

}
