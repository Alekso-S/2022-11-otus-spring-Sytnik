package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.dto.TaskRecord;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@DisplayName("Объект связи с данными")
class TestingDaoCsvTest {

    private final String csvFileName = "questions.csv";

    private final static int RECORDS_COUNT = 4;
    private final static List<TaskRecord> TASK_RECORDS = List.of(
            new TaskRecord(1, "Some question 1", "Some answer 1 to question 1", true),
            new TaskRecord(1, "Some question 1", "Some answer 2 to question 1", false),
            new TaskRecord(2, "Some question 2", "Some answer 1 to question 2", false),
            new TaskRecord(2, "Some question 2", "Some answer 2 to question 2", true)
    );

    @DisplayName("должен вернуть список записей")
    @Test
    void getAllRecords() {
        TestingDao testingDao = new TestingDaoCsv(csvFileName);

        List<TaskRecord> taskRecords = testingDao.getAllRecords();

        assertEquals(RECORDS_COUNT, taskRecords.size());
        assertIterableEquals(TASK_RECORDS, taskRecords);
    }
}