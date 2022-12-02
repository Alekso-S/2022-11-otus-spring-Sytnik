package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.domain.Record;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@TestPropertySource("classpath:test.properties")
//@SpringBootTest(classes = TestingDaoCsvTest.class)
@ExtendWith(SpringExtension.class)
class TestingDaoCsvTest {

    @Value("${csv-file-name}")
    private String csvFileName;

    private final static int RECORDS_COUNT = 4;
    private final static List<Record> RECORDS = List.of(
            new Record(1, "Some question 1", "Some answer 1 to question 1", true),
            new Record(1, "Some question 1", "Some answer 2 to question 1", false),
            new Record(2, "Some question 2", "Some answer 1 to question 2", false),
            new Record(2, "Some question 2", "Some answer 2 to question 2", true)
    );

    @Test
    void getAllRecords() {
        TestingDao testingDao = new TestingDaoCsv(csvFileName);

        List<Record> records = testingDao.getAllRecords();

        assertEquals(RECORDS_COUNT, records.size());
        assertIterableEquals(RECORDS, records);
    }
}