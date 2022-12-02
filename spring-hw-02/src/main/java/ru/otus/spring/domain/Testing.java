package ru.otus.spring.domain;

import org.springframework.stereotype.Component;
import ru.otus.spring.dao.TestingDao;
import java.util.ArrayList;
import java.util.List;

/**
 * Тестирование.
 * Содержит набор заданий.
 */
@Component
public class Testing {

    private final List<Task> tasks = new ArrayList<>();

    public Testing(TestingDao testingDao) {

        List<Record> records = testingDao.getAllRecords();

        for (Record record : records) {
            if (tasks.size() < record.getId()) {
                tasks.add(new Task(new Question(record.getQuestion())));
            }
            tasks.get(record.getId() - 1).getAnswers().add(new Answer(record.getAnswer(), record.isValidity()));
        }
    }

    public List<Task> getAllTasks() {
        return tasks;
    }

    public int getTasksCount() {
        return tasks.size();
    }

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();

        for (Task task: tasks) {
            questions.add(task.getQuestion());
        }

        return questions;
    }

    public Question getQuestion(int id) {
        return tasks.get(id).getQuestion();
    }

    public List<Answer> getAnswersToQuestion(int id) {
        return tasks.get(id).getAnswers();
    }
}
