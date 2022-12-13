package ru.otus.spring.domain;

import java.util.List;

/**
 * Экземпляр тестирования.
 * Содержит студента, набор вопросов и данные ответы
 */
public class TestingInstance {

    private final List<Question> questions;
    private final Student student;
    private List<Integer> answerNumbers;

    public TestingInstance(List<Question> questions, Student student) {
        this.questions = questions;
        this.student = student;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setAnswerNumbers(List<Integer> answerNumbers) {
        this.answerNumbers = answerNumbers;
    }

    public Student getStudent() {
        return student;
    }

    public List<Integer> getAnswerNumbers() {
        return answerNumbers;
    }
}
