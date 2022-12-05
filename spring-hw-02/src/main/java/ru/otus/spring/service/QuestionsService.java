package ru.otus.spring.service;

import ru.otus.spring.domain.Question;

import java.util.List;

/**
 * Тест.
 * Загружает и хранит набор вопросов
 */
public interface QuestionsService {
    List<Question> getQuestions();
}
