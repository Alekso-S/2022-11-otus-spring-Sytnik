package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.dao.TestingDao;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.exceptions.QuestionsExistenceException;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис тестирования должен")
class TestingServiceImplTest {

    @Mock
    private TestingDao testingDao;
    @Mock
    private StudentService studentService;
    @Mock
    private PrinterService printerService;
    private final double passCoefficient = 0.6;
    private TestingServiceImpl testingService;

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

    @BeforeEach
    public void setUp() {
        testingService = new TestingServiceImpl(testingDao, studentService, printerService, passCoefficient);

        lenient().when(testingDao.getQuestions()).thenReturn(getQuestions());
        lenient().when(studentService.requestStudent()).thenReturn(new Student(STUDENT_NAME, STUDENT_SURNAME));
        lenient().when(printerService.askForAnswer(getQuestions().size())).thenReturn("1", "2");
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

        testingService.run();

        verify(printerService, times(1)).printTestLoadErrorMessage();
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
        for (int i = 0; i < questions.size(); i++) {
            verify(printerService, times(1))
                    .printQuestionWithAnswers(questions.get(i), i + 1, questions.size());
        }
    }

    @DisplayName("запросить ответы на вопросы")
    @Test
    void shouldRequestAnswersToQuestions() {
        testingService.run();

        verify(printerService, times(2)).askForAnswer(getQuestions().size());
    }

    @DisplayName("запросить ввод повторно при недопустимом варианте ответа на вопрос")
    @Test
    void shouldRequestAnswersToQuestionsRepeatedly() {
        when(printerService.askForAnswer(getQuestions().size())).thenReturn("1", "3", "2");

        testingService.run();

        verify(printerService, times(3)).askForAnswer(getQuestions().size());
    }

    @DisplayName("вывести результат успешного тестирования")
    @Test
    void shouldPrintSuccessfulResult() {
        testingService.run();

        verify(printerService, times(1)).printPointsCnt(
                STUDENT_NAME + " " + STUDENT_SURNAME,
                2,
                getQuestions().size());

        verify(printerService, times(1)).printTestResult(true);
    }

    @DisplayName("вывести результат неуспешного тестирования")
    @Test
    void shouldPrintUnsuccessfulResult() {
        when(printerService.askForAnswer(getQuestions().size())).thenReturn("1", "1");

        testingService.run();

        verify(printerService, times(1)).printPointsCnt(
                STUDENT_NAME + " " + STUDENT_SURNAME,
                1,
                getQuestions().size());

        verify(printerService, times(1)).printTestResult(false);
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