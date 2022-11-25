package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.communication.Producer;
import ru.otus.spring.domain.*;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestingServiceImplTest {

    @Mock
    private Producer producer;
    @Mock
    private Testing testing;
    @InjectMocks
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

    @Test
    void printQuestions() {
        doReturn(getAllQuestions()).when(testing).getAllQuestions();

        testingService.printQuestions();

        verify(producer, times(1)).printLine("- " + QUESTION1_TEXT);
        verify(producer, times(1)).printLine("- " + QUESTION2_TEXT);
    }

    @Test
    void printQuestionsWithAnswers() {
        doReturn(getAllTasks()).when(testing).getAllTasks();

        testingService.printQuestionsWithAnswers();

        verify(producer, times(1)).printLine("- " + QUESTION1_TEXT);
        verify(producer, times(1)).printLine("|- (" + (ANSWER11_VALIDITY ? "+" : "-") + ") " + ANSWER11_TEXT);
        verify(producer, times(1)).printLine("|- (" + (ANSWER12_VALIDITY ? "+" : "-") + ") " + ANSWER12_TEXT);
        verify(producer, times(1)).printLine("- " + QUESTION2_TEXT);
        verify(producer, times(1)).printLine("|- (" + (ANSWER21_VALIDITY ? "+" : "-") + ") " + ANSWER21_TEXT);
        verify(producer, times(1)).printLine("|- (" + (ANSWER22_VALIDITY ? "+" : "-") + ") " + ANSWER22_TEXT);
    }

    private List<Question> getAllQuestions() {
        return List.of(
                new Question(QUESTION1_TEXT),
                new Question(QUESTION2_TEXT)
        );
    }

    private List<Task> getAllTasks() {
        Task task1 = new Task(new Question(QUESTION1_TEXT));
        task1.getAnswers().add(new Answer(ANSWER11_TEXT, ANSWER11_VALIDITY));
        task1.getAnswers().add(new Answer(ANSWER12_TEXT, ANSWER12_VALIDITY));
        Task task2 = new Task(new Question(QUESTION2_TEXT));
        task2.getAnswers().add(new Answer(ANSWER21_TEXT, ANSWER21_VALIDITY));
        task2.getAnswers().add(new Answer(ANSWER22_TEXT, ANSWER22_VALIDITY));

        return List.of(
                task1,
                task2
        );
    }
}