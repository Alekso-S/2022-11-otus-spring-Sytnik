package ru.otus.spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.io.IOService;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Сервис тестирования должен")
class TestingServiceImplTest {

    @Mock
    private IOService ioService;
    @Mock
    private QuestionsService questionsService;
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

    @BeforeEach
    public void setUp() {
        testingService = new TestingServiceImpl(questionsService, ioService, passCoefficient);
    }

    @DisplayName("получить список вопросов")
    @Test
    void shouldGetQuestions() {
        testingService.doTesting();

        verify(questionsService, times(1)).getQuestions();
    }

    @DisplayName("вывести сообщение об ошибке при отсутствии вопросов")
    @Test
    void shouldWriteErrorMessageOnQuestionsAbsence() {
        testingService.doTesting();

        verify(ioService, times(1)).writeLine("Test can not be loaded");
    }

    @DisplayName("запросить имя студента")
    @Test
    void shouldRequestStudent() {
        doReturn(getAllTasks()).when(questionsService).getQuestions();
        doReturn("Ivan Petrov").when(ioService).readLineWithRequest("Input your name and surname (ex. Ivan Petrov): ");
        doReturn("1").when(ioService).readLineWithRequest("\nType your answer [1-2]: ");
        doReturn("no").when(ioService).readLineWithRequest("\nType \"show\" if your want to see valid answers: ");

        testingService.doTesting();

        verify(ioService, times(1)).readLineWithRequest("Input your name and surname (ex. Ivan Petrov): ");
    }

    @DisplayName("вывести список вопросов")
    @Test
    void shouldPrintAllQuestions() {
        doReturn(getAllTasks()).when(questionsService).getQuestions();
        doReturn("Ivan Petrov").when(ioService).readLineWithRequest("Input your name and surname (ex. Ivan Petrov): ");
        doReturn("1").when(ioService).readLineWithRequest("\nType your answer [1-2]: ");
        doReturn("no").when(ioService).readLineWithRequest("\nType \"show\" if your want to see valid answers: ");

        testingService.doTesting();

        verify(ioService, times(1)).writeLine("\nQuestion 1 of 2:");
        verify(ioService, times(1)).writeLine("- Some question 1");
        verify(ioService, times(1)).writeLine("\nQuestion 2 of 2:");
        verify(ioService, times(1)).writeLine("- Some question 2");
    }

    @DisplayName("вывести варианты ответов на вопросы")
    @Test
    void shouldPrintAllAnswers() {
        doReturn(getAllTasks()).when(questionsService).getQuestions();
        doReturn("Ivan Petrov").when(ioService).readLineWithRequest("Input your name and surname (ex. Ivan Petrov): ");
        doReturn("1").when(ioService).readLineWithRequest("\nType your answer [1-2]: ");
        doReturn("no").when(ioService).readLineWithRequest("\nType \"show\" if your want to see valid answers: ");

        testingService.doTesting();

        verify(ioService, times(1)).writeLine("1) Some answer 1 to question 1");
        verify(ioService, times(1)).writeLine("2) Some answer 2 to question 1");
        verify(ioService, times(1)).writeLine("1) Some answer 1 to question 2");
        verify(ioService, times(1)).writeLine("2) Some answer 2 to question 2");
    }

    @DisplayName("запросить ответы на вопросы")
    @Test
    void shouldRequestAnswersToQuestions() {
        doReturn(getAllTasks()).when(questionsService).getQuestions();
        doReturn("Ivan Petrov").when(ioService).readLineWithRequest("Input your name and surname (ex. Ivan Petrov): ");
        doReturn("1").when(ioService).readLineWithRequest("\nType your answer [1-2]: ");
        doReturn("no").when(ioService).readLineWithRequest("\nType \"show\" if your want to see valid answers: ");

        testingService.doTesting();

        verify(ioService, times(2)).readLineWithRequest("\nType your answer [1-2]: ");
    }

    @DisplayName("запросить ввод повторно при недопустимом варианте ответа на вопрос")
    @Test
    void shouldRequestAnswersToQuestionsRepeatedly() {
        doReturn(getAllTasks()).when(questionsService).getQuestions();
        doReturn("Ivan Petrov").when(ioService).readLineWithRequest("Input your name and surname (ex. Ivan Petrov): ");
        doReturn("1", "3", "2").when(ioService).readLineWithRequest("\nType your answer [1-2]: ");
        doReturn("no").when(ioService).readLineWithRequest("\nType \"show\" if your want to see valid answers: ");

        testingService.doTesting();

        verify(ioService, times(3)).readLineWithRequest("\nType your answer [1-2]: ");
    }

    @DisplayName("вывести результат успешного тестирования")
    @Test
    void shouldPrintSuccessfulResult() {
        doReturn(getAllTasks()).when(questionsService).getQuestions();
        doReturn("Ivan Petrov").when(ioService).readLineWithRequest("Input your name and surname (ex. Ivan Petrov): ");
        doReturn("1", "2").when(ioService).readLineWithRequest("\nType your answer [1-2]: ");
        doReturn("no").when(ioService).readLineWithRequest("\nType \"show\" if your want to see valid answers: ");

        testingService.doTesting();

        verify(ioService, times(1)).writeLine("\nIvan Petrov, your have 2 points of 2");
        verify(ioService, times(1)).writeLine("Test is passed");
    }

    @DisplayName("вывести результат неуспешного тестирования")
    @Test
    void shouldPrintUnsuccessfulResult() {
        doReturn(getAllTasks()).when(questionsService).getQuestions();
        doReturn("Ivan Petrov").when(ioService).readLineWithRequest("Input your name and surname (ex. Ivan Petrov): ");
        doReturn("1", "1").when(ioService).readLineWithRequest("\nType your answer [1-2]: ");
        doReturn("no").when(ioService).readLineWithRequest("\nType \"show\" if your want to see valid answers: ");

        testingService.doTesting();

        verify(ioService, times(1)).writeLine("\nIvan Petrov, your have 1 points of 2");
        verify(ioService, times(1)).writeLine("Test is failed");
    }

    private List<Question> getAllTasks() {
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