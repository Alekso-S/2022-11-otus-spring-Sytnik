package ru.otus.spring.service;

import org.springframework.shell.Availability;
import org.springframework.stereotype.Service;
import ru.otus.spring.converter.QuestionConverter;
import ru.otus.spring.dao.TestingDao;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestingInstance;
import ru.otus.spring.exception.QuestionsExistenceException;
import ru.otus.spring.wrapper.MessageSourceWrapper;

import java.util.List;

@Service
public class TestingServiceImpl implements TestingService {

    private final TestingDao testingDao;
    private final StudentService studentService;
    private final ResultService resultService;
    private final MessageService messageService;
    private final MessageSourceWrapper messageSourceWrapper;

    Student student;

    public TestingServiceImpl(TestingDao testingDao,
                              StudentService studentService,
                              ResultService resultService,
                              MessageService messageService,
                              MessageSourceWrapper messageSourceWrapper) {
        this.testingDao = testingDao;
        this.studentService = studentService;
        this.resultService = resultService;
        this.messageService = messageService;
        this.messageSourceWrapper = messageSourceWrapper;
    }

    @Override
    public void setStudentName(String name) {
        student = studentService.getStudentByName(name);
    }

    @Override
    public void run() {
        try {
            TestingInstance testingInstance = new TestingInstance(
                    testingDao.getQuestions(),
                    student);

            doTesting(testingInstance);

            resultService.showResult(testingInstance);
            resultService.showValidAnswers(testingInstance);

        } catch (QuestionsExistenceException e) {
            printTestLoadErrorMessage();
        }
    }

    @Override
    public Availability getTestingAvailability() {
        if (student != null) {
            return Availability.available();
        } else {
            return Availability.unavailable(messageSourceWrapper.getMessage("student-name-missed"));
        }
    }

    @Override
    public void showInviteMessage() {
        messageService.send("student-name-request");
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
        messageService.send("test-load-problem");
    }

    private void printTestStartMessage() {
        messageService.sendNewLine("test-start");
    }

    private void printQuestionWithAnswers(Question question, int order, int count) {
        messageService.sendNewLine("test-question-number", order, count);
        messageService.sendNativeText(QuestionConverter.toText(question));
    }

    private String askForAnswer(int answersCnt) {
        return messageService.sendNewLineWithRequest("test-answer-request", answersCnt);
    }

    private void printInvalidAnswerMessage(String typedString) {
        messageService.send("test-invalid-answer", typedString);
    }
}
