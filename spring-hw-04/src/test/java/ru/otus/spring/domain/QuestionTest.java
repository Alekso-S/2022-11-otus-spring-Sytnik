package ru.otus.spring.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс вопроса")
class QuestionTest {

    private static final String TEXT = "Some question";
    Question question;

    @BeforeEach
    void setUp() {
        question = new Question(TEXT);
    }

    @DisplayName("должен вернуть значения полей")
    @Test
    void getters() {
        assertEquals(TEXT, question.getText());
    }
}