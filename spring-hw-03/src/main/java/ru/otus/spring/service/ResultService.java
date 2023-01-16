package ru.otus.spring.service;

import ru.otus.spring.domain.TestingInstance;

/**
 * Сервис "результата".
 * Выводит итоги тестирования и список правильных ответов
 */
public interface ResultService {
    void showResult(TestingInstance testing);

    void showValidAnswers(TestingInstance testing);
}
