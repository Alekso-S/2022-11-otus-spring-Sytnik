package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.TestingDao;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestingInstance;
import ru.otus.spring.exceptions.QuestionsExistenceException;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestingServiceImpl implements TestingService {

    private final TestingDao testingDao;
    private final StudentService studentService;
    private final PrinterService printerService;
    private final double passCoefficient;

    public TestingServiceImpl(TestingDao testingDao,
                              StudentService studentService,
                              PrinterService printerService,
                              @Value("${pass-coefficient}") double passCoefficient) {
        this.testingDao = testingDao;
        this.printerService = printerService;
        this.passCoefficient = passCoefficient;
        this.studentService = studentService;
    }


    @Override
    public void run() {
        try {
            TestingInstance testing = new TestingInstance(
                    testingDao.getQuestions(),
                    studentService.requestStudent());

            doTesting(testing);
            showResult(testing);

            showValidAnswers(testing);

        } catch (QuestionsExistenceException e) {
            printerService.printTestLoadErrorMessage();
        }
    }


    private void doTesting(TestingInstance testing) {
        printerService.printTestStartMessage();

        List<Question> questions = testing.getQuestions();
        List<Integer> answerNumbers = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            printerService.printQuestionWithAnswers(question, i + 1, questions.size());
            answerNumbers.add(requestAnswerToQuestion(question));
        }

        testing.setAnswerNumbers(answerNumbers);
    }

    private void showValidAnswers(TestingInstance testing) {
        if (!printerService.askIfToShowAnswers()) {
            return;
        }

        printerService.printValidAnswers(testing.getQuestions());
    }

    private void showResult(TestingInstance testing) {
        Student student = testing.getStudent();
        List<Question> questions = testing.getQuestions();
        int validAnswersCnt = getValidAnswersCnt(testing);

        printerService.printPointsCnt(student.getFullName(), validAnswersCnt, questions.size());
        printerService.printTestResult(validAnswersCnt >= questions.size() * passCoefficient);
    }

    private static int getValidAnswersCnt(TestingInstance testing) {
        int result = 0;

        List<Question> questions = testing.getQuestions();
        List<Integer> answerNumbers = testing.getAnswerNumbers();
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
            String typedString = printerService.askForAnswer(answers.size());

            if (typedString.matches("[1-" + answers.size() + "]")) {
                return Integer.parseInt(typedString);
            } else {
                printerService.printInvalidAnswerMessage(typedString);
            }
        }
    }
}
