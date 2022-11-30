package ru.otus.spring.domain;

/**
 * Запись денормализованной таблицы источника.
 */
public class Record {

    private int id;
    private String question;
    private String answer;
    private boolean validity;

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
}
