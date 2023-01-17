package ru.otus.spring.service;

import org.springframework.shell.Availability;

/**
 * Основной сервис тестирования
 */
public interface TestingService {

    void setStudentName(String name);

    void run();

    Availability getTestingAvailability();

    void showInviteMessage();
}
