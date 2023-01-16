package ru.otus.spring.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.spring.config.DaoCsvProps;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.dto.TaskRecord;
import ru.otus.spring.exception.DataReadException;
import ru.otus.spring.exception.DataValidityException;
import ru.otus.spring.exception.QuestionsExistenceException;
import ru.otus.spring.validator.TaskRecordValidator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestingDaoCsv implements TestingDao {

    private static final Logger logger = LoggerFactory.getLogger(TestingDaoCsv.class);

    private final DaoCsvProps daoCsvProps;

    public TestingDaoCsv(DaoCsvProps daoCsvProps) {
        this.daoCsvProps = daoCsvProps;
    }

    @Override
    public List<Question> getQuestions() throws QuestionsExistenceException {
        List<Question> questions = new ArrayList<>();

        try {
            for (TaskRecord taskRecord : getRecords()) {
                TaskRecordValidator.checkValidity(taskRecord);

                if (questions.size() < taskRecord.getId()) {
                    questions.add(new Question(taskRecord.getQuestion()));
                }
                questions.get(taskRecord.getId() - 1).getAnswers().add(
                        new Answer(taskRecord.getAnswer(), taskRecord.isValid())
                );
            }
        } catch (DataReadException e) {
            logger.error("Error during loading the source file: " + e.getMessage());
        } catch (DataValidityException e) {
            logger.error("Error during parsing data from the source file: " + e.getMessage());
        }

        if (questions.size() > 0) {
            return questions;
        }
        else {
            throw new QuestionsExistenceException("Test can not be loaded");
        }
    }

    private List<TaskRecord> getRecords() throws DataReadException {
        try (InputStream resourceInputStream = getClass().getResourceAsStream("/" + daoCsvProps.getCsvFileName())) {
            HeaderColumnNameMappingStrategy<TaskRecord> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
            mappingStrategy.setType(TaskRecord.class);

            InputStreamReader inputStreamReader = new InputStreamReader(resourceInputStream);

            CsvToBean<TaskRecord> csvToBean = new CsvToBeanBuilder<TaskRecord>(inputStreamReader)
                    .withMappingStrategy(mappingStrategy)
                    .build();

            return csvToBean.parse();
        } catch (IOException | RuntimeException e) {
            throw new DataReadException(e);
        }
    }
}

