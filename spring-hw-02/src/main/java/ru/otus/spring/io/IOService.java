package ru.otus.spring.io;

/**
 * Интерфейс взаимодействия с пользователем
 */
public interface IOService {
    void writeString(String s);

    void writeLine(String s);

    String readLine();

    String readLineWithRequest(String s);
}
