package ru.otus.spring.service;

import org.springframework.stereotype.Component;
import ru.otus.spring.dao.TestingDao;
import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.dto.TaskRecord;
import ru.otus.spring.exceptions.DataValidityException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class QuestionsServiceImpl implements QuestionsService {

    private final TestingDao testingDao;
    private List<Question> questions;

    public QuestionsServiceImpl(TestingDao testingDao) {
        this.testingDao = testingDao;
    }

    @Override
    public List<Question> getQuestions() {
        if (questions == null || questions.size() == 0) {
            loadQuestions();
        }

        return questions;
    }

    private void loadQuestions() {
        try {
            List<TaskRecord> taskRecords = testingDao.getAllRecords();
            questions = new ArrayList<>();

            for (TaskRecord taskRecord : taskRecords) {
                taskRecord.checkValidity();

                if (questions.size() < taskRecord.getId()) {
                    questions.add(new Question(taskRecord.getQuestion()));
                }
                questions.get(taskRecord.getId() - 1).getAnswers().add(
                        new Answer(taskRecord.getAnswer(), taskRecord.isValid())
                );
            }

        } catch (DataValidityException e) {
            Logger logger = Logger.getLogger("application");
            logger.severe(e.getMessage());
        }
    }
}
