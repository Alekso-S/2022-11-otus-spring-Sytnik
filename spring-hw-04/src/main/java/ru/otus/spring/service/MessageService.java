package ru.otus.spring.service;

public interface MessageService {
    void send(String code, Object... objects);

    void sendNewLine(String code, Object... objects);

    void sendNativeText(String message);

    String sendWithRequest(String code, Object... objects);

    String sendNewLineWithRequest(String code, Object... objects);
}
