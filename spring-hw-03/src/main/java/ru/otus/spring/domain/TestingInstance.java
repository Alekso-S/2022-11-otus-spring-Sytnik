package ru.otus.spring.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Экземпляр тестирования.
 * Содержит студента, набор вопросов и данные ответы
 */
public class TestingInstance {

    private final List<Question> questions;
    private final Student student;
    private final List<Integer> answerNumbers;

    public TestingInstance(List<Question> questions, Student student) {
        this.questions = questions;
        this.student = student;
        this.answerNumbers = new ArrayList<>();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Student getStudent() {
        return student;
    }

    public List<Integer> getAnswerNumbers() {
        return answerNumbers;
    }

    public void addAnswerNumber(int answerNumber) {
        answerNumbers.add(answerNumber);
    }
}
