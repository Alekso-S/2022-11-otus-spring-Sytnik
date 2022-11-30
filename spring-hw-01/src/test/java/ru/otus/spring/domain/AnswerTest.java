package ru.otus.spring.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.Assertions.assertThat;

class AnswerTest {

    private static final String TEXT = "Some answer";
    private static final boolean VALIDITY = true;
    private Answer answer;

    @BeforeEach
    void setUp() {
        answer = new Answer(TEXT, VALIDITY);
    }

    @Test
    void getters() {
        assertThat(answer)
                .returns(TEXT, from(Answer::getText))
                .returns(VALIDITY, from(Answer::isValidity));
    }
}
