package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.config.AppProps;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
@DisplayName("Объект связи с данными")
class TestingDaoCsvTest {

    @Autowired
    private AppProps appProps;

    @DisplayName("должен вернуть список вопросов")
    @Test
    void getQuestions() {
        TestingDaoCsv testingDao = new TestingDaoCsv(appProps);

        List<Question> questions = testingDao.getQuestions();

        assertIterableEquals(QUESTIONS, questions);
    }

    private final static List<Question> QUESTIONS = List.of(
            new Question("Some question 1", List.of(
                    new Answer("Some answer 1 to question 1", true),
                    new Answer("Some answer 2 to question 1", false)
            )),
            new Question("Some question 2", List.of(
                    new Answer("Some answer 1 to question 2", false),
                    new Answer("Some answer 2 to question 2", true)
            ))
    );
}