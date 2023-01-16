package ru.otus.spring.service;

import ru.otus.spring.domain.Student;

/**
 * Сервис работы со студентом.
 * Создаёт студента на запрошенных данных
 */
public interface StudentService {
    Student requestStudent();

    Student getStudentByName(String name);
}
