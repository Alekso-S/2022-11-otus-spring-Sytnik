package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.converter.QuestionConverter;
import ru.otus.spring.dao.TestingDao;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.TestingInstance;
import ru.otus.spring.exception.QuestionsExistenceException;
import ru.otus.spring.io.IOService;
import ru.otus.spring.wrapper.MessageSourceWrapper;

import java.util.List;

@Service
public class TestingServiceImpl implements TestingService {

    private final TestingDao testingDao;
    private final StudentService studentService;
    private final IOService ioService;
    private final ResultService resultService;
    private final MessageSourceWrapper messageSourceWrapper;

    public TestingServiceImpl(TestingDao testingDao,
                              StudentService studentService,
                              IOService ioService,
                              ResultService resultService,
                              MessageSourceWrapper messageSourceWrapper) {
        this.testingDao = testingDao;
        this.studentService = studentService;
        this.ioService = ioService;
        this.resultService = resultService;
        this.messageSourceWrapper = messageSourceWrapper;
    }

    @Override
    public void run() {
        try {
            TestingInstance testingInstance = new TestingInstance(
                    testingDao.getQuestions(),
                    studentService.requestStudent());

            doTesting(testingInstance);

            resultService.showResult(testingInstance);
            resultService.showValidAnswers(testingInstance);

        } catch (QuestionsExistenceException e) {
            printTestLoadErrorMessage();
        }
    }

    private void doTesting(TestingInstance testingInstance) {
        printTestStartMessage();

        List<Question> questions = testingInstance.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            printQuestionWithAnswers(question, i + 1, questions.size());
            testingInstance.addAnswerNumber(requestAnswerToQuestion(question));
        }
    }

    private int requestAnswerToQuestion(Question question) {
        List<Answer> answers = question.getAnswers();

        while (true) {
            String typedString = askForAnswer(answers.size());

            if (typedString.matches("[1-" + answers.size() + "]")) {
                return Integer.parseInt(typedString);
            } else {
                printInvalidAnswerMessage(typedString);
            }
        }
    }

    private void printTestLoadErrorMessage() {
        ioService.writeLine(messageSourceWrapper.getMessage("test-load-problem"));
    }

    private void printTestStartMessage() {
        ioService.writeLine("\n" + messageSourceWrapper.getMessage("test-start"));
    }

    private void printQuestionWithAnswers(Question question, int order, int count) {
        ioService.writeLine("\n" +
                messageSourceWrapper.getMessage("test-question-number", new Object[]{order, count}));
        ioService.writeLine(QuestionConverter.toText(question));
    }

    private String askForAnswer(int answersCnt) {
        return ioService.readLineWithRequest("\n" +
                messageSourceWrapper.getMessage("test-answer-request", new Object[]{answersCnt}) + ": ");
    }

    private void printInvalidAnswerMessage(String typedString) {
        ioService.writeLine(messageSourceWrapper.getMessage("test-invalid-answer", new Object[]{typedString}));
    }
}
