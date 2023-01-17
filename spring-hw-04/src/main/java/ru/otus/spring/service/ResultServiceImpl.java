package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import ru.otus.spring.config.ResultServiceProps;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestingInstance;

import java.util.List;

@Component
public class ResultServiceImpl implements ResultService {

    private final ResultServiceProps resultServiceProps;
    private final MessageService messageService;

    public ResultServiceImpl(ResultServiceProps resultServiceProps,
                             MessageService messageService) {
        this.resultServiceProps = resultServiceProps;
        this.messageService = messageService;
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
        messageService.send("test-points-cnt", fullName, validAnswersCnt, questionsCnt);
    }

    private void printTestResult(boolean isPassed) {
        messageService.send(isPassed ? "test-result-pass" : "test-result-fail");
    }

    private boolean askIfToShowAnswers() {
        return messageService.sendNewLineWithRequest("test-show-answers-request").equals("show");
    }

    private void printValidAnswers(List<Question> questions) {
        for (Question question : questions) {
            messageService.sendNativeText("- " + question.getText());
            for (Answer answer : question.getAnswers()) {
                messageService.sendNativeText("|- (" + (answer.isValid() ? "+" : "-") + ") " + answer.getText());
            }
        }
    }
}
