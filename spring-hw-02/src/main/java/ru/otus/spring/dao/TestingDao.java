package ru.otus.spring.dao;

import ru.otus.spring.dto.TaskRecord;

import java.util.List;

/**
 * Интерфейс связи с источником данных.
 * Позволяет получить список записей
 */
public interface TestingDao {
    List<TaskRecord> getAllRecords();
}
