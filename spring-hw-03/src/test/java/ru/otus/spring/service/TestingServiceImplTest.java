package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.converter.QuestionConverter;
import ru.otus.spring.dao.TestingDao;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestingInstance;
import ru.otus.spring.exception.QuestionsExistenceException;
import ru.otus.spring.io.IOService;
import ru.otus.spring.wrapper.MessageSourceWrapper;

import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("Сервис тестирования должен")
@SpringBootTest
class TestingServiceImplTest {

    private static final String QUESTION1_TEXT = "Some question 1";
    private static final String ANSWER11_TEXT = "Some answer 1 to question 1";
    private static final boolean ANSWER11_VALIDITY = true;
    private static final String ANSWER12_TEXT = "Some answer 2 to question 1";
    private static final boolean ANSWER12_VALIDITY = false;
    private static final String QUESTION2_TEXT = "Some question 2";
    private static final String ANSWER21_TEXT = "Some answer 1 to question 2";
    private static final boolean ANSWER21_VALIDITY = false;
    private static final String ANSWER22_TEXT = "Some answer 2 to question 2";
    private static final boolean ANSWER22_VALIDITY = true;
    private static final String STUDENT_NAME = "Ivan";
    private static final String STUDENT_SURNAME = "Petrov";

    @MockBean
    private TestingDao testingDao;
    @MockBean
    private StudentService studentService;
    @MockBean
    private IOService ioService;
    @MockBean
    private ResultService resultService;
    @MockBean
    private MessageSourceWrapper messageSourceWrapper;
    @Autowired
    private TestingServiceImpl testingService;

    @BeforeEach
    public void setUp() {
        lenient().when(testingDao.getQuestions()).thenReturn(getQuestions());
        lenient().when(studentService.requestStudent()).thenReturn(new Student(STUDENT_NAME, STUDENT_SURNAME));
        lenient().when(ioService.readLineWithRequest("\nType your answer [1-" + getQuestions().size() + "]: "))
                .thenReturn("1", "2");
        lenient().when(messageSourceWrapper.getMessage(eq("test-answer-request"), any()))
                .thenReturn("Type your answer [1-" + getQuestions().size() + "]");

    }

    @DisplayName("получить список вопросов")
    @Test
    void shouldGetQuestions() {
        testingService.run();

        verify(testingDao, times(1)).getQuestions();
    }

    @DisplayName("вывести сообщение об ошибке при отсутствии вопросов")
    @Test
    void shouldWriteErrorMessageOnQuestionsAbsence() {
        when(testingDao.getQuestions()).thenThrow(new QuestionsExistenceException(""));
        lenient().when(messageSourceWrapper.getMessage(eq("test-load-problem")))
                .thenReturn("Test can not be loaded");

        testingService.run();

        verify(ioService, times(1)).writeLine("Test can not be loaded");
    }

    @DisplayName("запросить студента")
    @Test
    void shouldRequestStudent() {
        testingService.run();

        verify(studentService, times(1)).requestStudent();
    }

    @DisplayName("вывести вопросы с вариантами ответов")
    @Test
    void shouldPrintQuestionWithAnswers() {
        testingService.run();

        List<Question> questions = getQuestions();
        for (Question question : questions) {
            verify(ioService, times(1))
                    .writeLine(QuestionConverter.toText(question));
        }
    }

    @DisplayName("запросить ответы на вопросы")
    @Test
    void shouldRequestAnswersToQuestions() {
        testingService.run();

        verify(ioService, times(2))
                .readLineWithRequest("\nType your answer [1-" + getQuestions().size() + "]: ");
    }

    @DisplayName("запросить ввод повторно при недопустимом варианте ответа на вопрос")
    @Test
    void shouldRequestAnswersToQuestionsRepeatedly() {
        when(ioService.readLineWithRequest("\nType your answer [1-" + getQuestions().size() + "]: "))
                .thenReturn("1", "3", "2");

        testingService.run();

        verify(ioService, times(3))
                .readLineWithRequest("\nType your answer [1-" + getQuestions().size() + "]: ");
    }

    @DisplayName("вывести результат тестирования")
    @Test
    void shouldPrintSuccessfulResult() {
        testingService.run();

        verify(resultService, times(1)).showResult(any(TestingInstance.class));
    }

    private List<Question> getQuestions() {
        Question question1 = new Question(QUESTION1_TEXT);
        question1.getAnswers().add(new Answer(ANSWER11_TEXT, ANSWER11_VALIDITY));
        question1.getAnswers().add(new Answer(ANSWER12_TEXT, ANSWER12_VALIDITY));
        Question question2 = new Question(QUESTION2_TEXT);
        question2.getAnswers().add(new Answer(ANSWER21_TEXT, ANSWER21_VALIDITY));
        question2.getAnswers().add(new Answer(ANSWER22_TEXT, ANSWER22_VALIDITY));

        return List.of(
                question1,
                question2
        );
    }
}