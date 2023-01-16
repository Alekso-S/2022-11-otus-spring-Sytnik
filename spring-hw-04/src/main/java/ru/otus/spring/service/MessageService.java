package ru.otus.spring.service;

public interface MessageService {
    void Send(String code, Object... objects);

    void SendNewLine(String code, Object... objects);

    void SendNativeText(String message);

    String SendWithRequest(String code, Object... objects);

    String SendNewLineWithRequest(String code, Object... objects);
}
