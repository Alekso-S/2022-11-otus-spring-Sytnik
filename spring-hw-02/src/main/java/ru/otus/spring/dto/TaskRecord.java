package ru.otus.spring.dto;

import ru.otus.spring.exceptions.DataValidityException;

import java.util.Objects;

/**
 * Запись денормализованной таблицы источника.
 */
public class TaskRecord {

    private Integer id;
    private String question;
    private String answer;
    private Boolean valid;

    public TaskRecord(int id, String question, String answer, boolean valid) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.valid = valid;
    }

    public TaskRecord() {
    }

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public String toString() {
        return "TaskRecord{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", validity=" + valid +
                '}';
    }

    public void checkValidity() {
        if (id == null || question == null || answer == null || valid == null)
            throw new DataValidityException("Error during reading data from source file");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskRecord that = (TaskRecord) o;
        return id.equals(that.id) && question.equals(that.question) && answer.equals(that.answer) && valid.equals(that.valid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, answer, valid);
    }
}
