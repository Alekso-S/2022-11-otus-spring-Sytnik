package ru.otus.spring.service;

import ru.otus.spring.communication.Producer;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Task;
import ru.otus.spring.domain.Testing;

public class TestingServiceImpl implements TestingService {

    private final Testing testing;
    private final Producer producer;

    public TestingServiceImpl(Testing testing, Producer producer) {
        this.testing = testing;
        this.producer = producer;
    }

    @Override
    public void printQuestions() {
        for (Question question : testing.getAllQuestions()) {
            producer.printLine("- " + question.getText());
        }
    }

    @Override
    public void printQuestionsWithAnswers() {
        for (Task task : testing.getAllTasks()) {
            producer.printLine("- " + task.getQuestion().getText());
            for (Answer answer : task.getAnswers()) {
                producer.printLine("|- (" + (answer.isValidity() ? "+" : "-") + ") " + answer.getText());
            }
        }
    }
}
