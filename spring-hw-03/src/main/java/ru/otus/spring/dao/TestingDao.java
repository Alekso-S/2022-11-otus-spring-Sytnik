package ru.otus.spring.dao;

import ru.otus.spring.domain.Question;
import ru.otus.spring.exception.QuestionsExistenceException;

import java.util.List;

/**
 * Интерфейс связи с источником данных.
 * Позволяет получить список вопросов
 */
public interface TestingDao {

    List<Question> getQuestions() throws QuestionsExistenceException;
}
