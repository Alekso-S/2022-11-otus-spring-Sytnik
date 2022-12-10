package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.annotations.QuestionBenchmark;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.io.IOService;

import java.util.List;

@Service
public class PrinterServiceImpl implements PrinterService {

    private final IOService ioService;

    public PrinterServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    @Override
    public void printTestLoadErrorMessage() {
        ioService.writeLine("Test can not be loaded");
    }

    @Override
    public void printTestStartMessage() {
        ioService.writeLine("\nTesting is starting...");
    }

    @Override
    public boolean askIfToShowAnswers() {
        return ioService.readLineWithRequest("\nType \"show\" if your want to see valid answers: ").equals("show");
    }

    @Override
    public void printValidAnswers(List<Question> questions) {
        for (Question question : questions) {
            ioService.writeLine("- " + question.getText());
            for (Answer answer : question.getAnswers()) {
                ioService.writeLine("|- (" + (answer.isValid() ? "+" : "-") + ") " + answer.getText());
            }
        }
    }

    @Override
    public void printPointsCnt(String fullName, int validAnswersCnt, int questionsCnt) {
        ioService.writeLine("\n" + fullName + ", your have " + validAnswersCnt + " points of " + questionsCnt);
    }

    @Override
    public void printTestResult(boolean isPassed) {
        ioService.writeLine("Test is " + (isPassed ? "passed" : "failed"));
    }

    @Override
    public void printInvalidAnswerMessage(String typedString) {
        ioService.writeLine("Answer \"" + typedString + "\" is not valid");
    }

    @QuestionBenchmark
    @Override
    public String askForAnswer(int answersCnt) {
        return ioService.readLineWithRequest("\nType your answer [1-" + answersCnt + "]: ");
    }

    @Override
    public void printQuestionWithAnswers(Question question, int order, int count) {
        ioService.writeLine("\nQuestion " + order + " of " + count + ":");
        ioService.writeLine("- " + question.getText());

        List<Answer> answers = question.getAnswers();
        for (int j = 0; j < answers.size(); j++) {
            ioService.writeLine((j + 1) + ") " + answers.get(j).getText());
        }
    }

    @Override
    public String askForName() {
        return ioService.readLineWithRequest("Input your name and surname (ex. Ivan Petrov): ");
    }
}
