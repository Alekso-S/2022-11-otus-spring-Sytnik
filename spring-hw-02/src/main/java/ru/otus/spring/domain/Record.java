package ru.otus.spring.domain;

import java.util.Objects;

/**
 * Запись денормализованной таблицы источника.
 */
public class Record {

    private int id;
    private String question;
    private String answer;
    private boolean validity;

    public Record(int id, String question, String answer, boolean validity) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.validity = validity;
    }

    public Record() {
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

    public boolean isValidity() {
        return validity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return id == record.id && validity == record.validity && question.equals(record.question) && answer.equals(record.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question, answer, validity);
    }
}
