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
    private ResultService resultService;
    @MockBean
    private MessageService messageService;
    @Autowired
    private TestingServiceImpl testingService;

    @BeforeEach
    public void setUp() {
        lenient().when(testingDao.getQuestions()).thenReturn(getQuestions());
        lenient().when(studentService.requestStudent()).thenReturn(new Student(STUDENT_NAME, STUDENT_SURNAME));
        lenient().when(messageService.sendNewLineWithRequest("test-answer-request",getQuestions().size()))
                        .thenReturn("1", "2");
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

        verify(messageService, times(1)).send("test-load-problem");
    }

    @DisplayName("вывести вопросы с вариантами ответов")
    @Test
    void shouldPrintQuestionWithAnswers() {
        testingService.run();

        List<Question> questions = getQuestions();
        for (Question question : questions) {
            verify(messageService, times(1)).sendNativeText(QuestionConverter.toText(question));
        }
    }

    @DisplayName("запросить ответы на вопросы")
    @Test
    void shouldRequestAnswersToQuestions() {
        testingService.run();

        verify(messageService, times(2))
                .sendNewLineWithRequest("test-answer-request", 2);
    }

    @DisplayName("запросить ввод повторно при недопустимом варианте ответа на вопрос")
    @Test
    void shouldRequestAnswersToQuestionsRepeatedly() {
        lenient().when(messageService.sendNewLineWithRequest("test-answer-request",getQuestions().size()))
                .thenReturn("1", "3", "2");

        testingService.run();

        verify(messageService, times(3))
                .sendNewLineWithRequest("test-answer-request", 2);
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