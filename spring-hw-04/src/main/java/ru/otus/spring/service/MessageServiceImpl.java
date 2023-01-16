package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.io.IOService;
import ru.otus.spring.wrapper.MessageSourceWrapper;

@Service
public class MessageServiceImpl implements MessageService {

    private final IOService ioService;
    private final MessageSourceWrapper messageSourceWrapper;

    public MessageServiceImpl(IOService ioService, MessageSourceWrapper messageSourceWrapper) {
        this.ioService = ioService;
        this.messageSourceWrapper = messageSourceWrapper;
    }

    @Override
    public void Send(String code, Object... objects) {
        ioService.writeLine(messageSourceWrapper.getMessage(code, objects));
    }

    @Override
    public void SendNewLine(String code, Object... objects) {
        ioService.writeLine("\n" + messageSourceWrapper.getMessage(code, objects));
    }

    @Override
    public void SendNativeText(String message) {
        ioService.writeLine(message);
    }

    @Override
    public String SendWithRequest(String code, Object... objects) {
        return ioService.writeLineWithRequest(messageSourceWrapper.getMessage(code, objects) + ": ");
    }

    @Override
    public String SendNewLineWithRequest(String code, Object... objects) {
        return ioService.writeLineWithRequest("\n" + messageSourceWrapper.getMessage(code, objects) + ": ");
    }
}
