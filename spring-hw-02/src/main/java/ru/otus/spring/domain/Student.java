package ru.otus.spring.domain;

/**
 * Проходящий тестирование студент.
 * Содержит имя и фамилию
 */
public class Student {

    private final String name;
    private final String surname;

    public Student(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getFullName() {
        return name + " " + surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
