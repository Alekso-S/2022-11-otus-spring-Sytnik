package ru.otus.spring.connection;

/**
 * Отправитель сообщений пользователю
 */
public interface Connector {
    void sendText(String s);

    void sendLine(String s);

    String getLine();
}
