package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestingInstance;

import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("Сервис отображения результата должен")
@SpringBootTest
class ResultServiceImplTest {

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
    private TestingInstance testingInstance;
    @MockBean
    private MessageService messageService;
    @Autowired
    private ResultServiceImpl resultService;

    @BeforeEach
    void setUp() {
        when(testingInstance.getStudent()).thenReturn(new Student(STUDENT_NAME, STUDENT_SURNAME));
        when(testingInstance.getQuestions()).thenReturn(getQuestions());
        when(testingInstance.getAnswerNumbers()).thenReturn(getAnswerNumbers());
    }

    @DisplayName("выводит число набранных баллов")
    @Test
    void shouldShowPointsCnt() {
        resultService.showResult(testingInstance);

        verify(messageService, times(1)).send(eq("test-points-cnt"), any());
    }

    @DisplayName("выводит результат тестирования")
    @Test
    void shouldShowResult() {
        resultService.showResult(testingInstance);

        verify(messageService, times(1)).send("test-result-pass");
    }

    @DisplayName("выводит правильные варианты ответов")
    @Test
    void shouldShowValidAnswers() {
        when(messageService.sendNewLineWithRequest("test-show-answers-request")).thenReturn("show");

        resultService.showValidAnswers(testingInstance);

        verify(testingInstance, times(1)).getQuestions();
        verify(messageService, times(6)).sendNativeText(any());
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

    private List<Integer> getAnswerNumbers() {
        return List.of(1, 2);
    }
}