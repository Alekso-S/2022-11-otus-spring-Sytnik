package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;
import ru.otus.spring.exceptions.QuestionsExistenceException;
import ru.otus.spring.io.IOService;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestingServiceImpl implements TestingService {

    private final QuestionsService questionsService;
    private final IOService ioService;
    private final double passCoefficient;

    public TestingServiceImpl(QuestionsService questionsService,
                              IOService ioService,
                              @Value("${pass-coefficient}") double passCoefficient) {
        this.questionsService = questionsService;
        this.ioService = ioService;
        this.passCoefficient = passCoefficient;
    }

    @Override
    public void doTesting() {
        try {
            List<Question> questions = questionsService.getQuestions();
            List<Integer> answerNumbers = new ArrayList<>();

            checkQuestionsExistence(questions);
            Student student = requestStudent();

            ioService.writeLine("\nTesting is starting...");

            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                printQuestionWithAnswers(question, i + 1, questions.size());
                answerNumbers.add(requestAnswerToQuestion(question));
            }

            int validAnswersCnt = getValidAnswersCnt(questions, answerNumbers);
            printResult(questions, student, validAnswersCnt);

            offerValidAnswers();

        } catch (QuestionsExistenceException e) {
            ioService.writeLine("Test can not be loaded");
        }
    }

    private void checkQuestionsExistence(List<Question> questions) {
        if (questions.size() == 0)
            throw new QuestionsExistenceException("Test can not be loaded");
    }

    private void offerValidAnswers() {
        if (!ioService.readLineWithRequest("\nType \"show\" if your want to see valid answers: ").equals("show"))
            return;

        try {
            List<Question> questions = questionsService.getQuestions();

            checkQuestionsExistence(questions);

            for (Question question : questions) {
                ioService.writeLine("- " + question.getText());
                for (Answer answer : question.getAnswers()) {
                    ioService.writeLine("|- (" + (answer.isValid() ? "+" : "-") + ") " + answer.getText());
                }
            }
        } catch (QuestionsExistenceException e) {
            ioService.writeLine(e.getMessage());
        }
    }

    private void printResult(List<Question> questions, Student student, int validAnswersCnt) {
        ioService.writeLine("\n" + student.getFullName() + ", your have " + validAnswersCnt + " points of " + questions.size());
        ioService.writeLine("Test is " + (validAnswersCnt >= questions.size() * passCoefficient ? "passed" : "failed"));
    }

    private static int getValidAnswersCnt(List<Question> questions, List<Integer> answerNumbers) {
        int result = 0;

        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getAnswers().get(answerNumbers.get(i) - 1).isValid()) {
                result++;
            }
        }
        return result;
    }

    private int requestAnswerToQuestion(Question question) {
        List<Answer> answers = question.getAnswers();

        while (true) {
            String typedString = ioService.readLineWithRequest("\nType your answer [1-" + answers.size() + "]: ");

            if (typedString.matches("[1-" + answers.size() + "]")) {
                return Integer.parseInt(typedString);
            } else {
                ioService.writeLine("Answer \"" + typedString + "\" is not valid");
            }
        }
    }

    private void printQuestionWithAnswers(Question question, int order, int count) {
        ioService.writeLine("\nQuestion " + order + " of " + count + ":");
        ioService.writeLine("- " + question.getText());

        List<Answer> answers = question.getAnswers();
        for (int j = 0; j < answers.size(); j++) {
            ioService.writeLine((j + 1) + ") " + answers.get(j).getText());
        }
    }

    private Student requestStudent() {
        String fullName;

        do {
            fullName = ioService.readLineWithRequest("Input your name and surname (ex. Ivan Petrov): ");
        } while (!fullName.matches(".+ .+"));

        return new Student(fullName.split(" ")[0], fullName.split(" ")[1]);
    }
}
