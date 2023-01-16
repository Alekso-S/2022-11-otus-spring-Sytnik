package ru.otus.spring.service;

/**
 * Основной сервис тестирования
 */
public interface TestingService {

    void setStudentName(String name);

    void run();

    boolean checkTestingAvailability();
}
