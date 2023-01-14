package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import ru.otus.spring.config.ResultServiceProps;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestingInstance;
import ru.otus.spring.io.IOService;
import ru.otus.spring.wrapper.MessageSourceWrapper;

import java.util.List;

@Component
public class ResultServiceImpl implements ResultService {

    private final IOService ioService;
    private final ResultServiceProps resultServiceProps;
    private final MessageSourceWrapper messageSourceWrapper;

    public ResultServiceImpl(IOService ioService,
                             ResultServiceProps resultServiceProps,
                             MessageSourceWrapper messageSourceWrapper) {
        this.ioService = ioService;
        this.resultServiceProps = resultServiceProps;
        this.messageSourceWrapper = messageSourceWrapper;
    }

    @Override
    public void showResult(TestingInstance testingInstance) {
        Student student = testingInstance.getStudent();
        List<Question> questions = testingInstance.getQuestions();
        int validAnswersCnt = getValidAnswersCnt(testingInstance);

        printPointsCnt(student.getFullName(), validAnswersCnt, questions.size());
        printTestResult(validAnswersCnt >= questions.size() * resultServiceProps.getPassCoefficient());
    }

    @Override
    public void showValidAnswers(TestingInstance testingInstance) {
        if (!askIfToShowAnswers()) {
            return;
        }

        printValidAnswers(testingInstance.getQuestions());
    }

    private int getValidAnswersCnt(TestingInstance testingInstance) {
        int result = 0;

        List<Question> questions = testingInstance.getQuestions();
        List<Integer> answerNumbers = testingInstance.getAnswerNumbers();
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getAnswers().get(answerNumbers.get(i) - 1).isValid()) {
                result++;
            }
        }
        return result;
    }

    private void printPointsCnt(String fullName, int validAnswersCnt, int questionsCnt) {
        ioService.writeLine(messageSourceWrapper.getMessage("test-points-cnt",
                new Object[]{fullName, validAnswersCnt, questionsCnt}));
    }

    private void printTestResult(boolean isPassed) {
        ioService.writeLine(messageSourceWrapper.getMessage(isPassed ? "test-result-pass" : "test-result-fail"));
    }

    private boolean askIfToShowAnswers() {
        return ioService.readLineWithRequest("\n" +
                messageSourceWrapper.getMessage("test-show-answers-request") + ": ").equals("show");
    }

    private void printValidAnswers(List<Question> questions) {
        for (Question question : questions) {
            ioService.writeLine("- " + question.getText());
            for (Answer answer : question.getAnswers()) {
                ioService.writeLine("|- (" + (answer.isValid() ? "+" : "-") + ") " + answer.getText());
            }
        }
    }
}
