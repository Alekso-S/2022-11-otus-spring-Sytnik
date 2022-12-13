package ru.otus.spring.service;

import ru.otus.spring.domain.Question;

import java.util.List;

public interface PrinterService {
    void printTestLoadErrorMessage();

    void printTestStartMessage();

    boolean askIfToShowAnswers();

    void printValidAnswers(List<Question> questions);

    void printPointsCnt(String fullName, int validAnswersCnt, int questionsCnt);

    void printTestResult(boolean isPassed);

    void printInvalidAnswerMessage(String typedString);

    String askForAnswer(int answersCnt);

    void printQuestionWithAnswers(Question question, int order, int count);

    String askForName();
}
