package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.io.IOService;
import ru.otus.spring.wrapper.MessageSourceWrapper;

@Service
public class MessageServiceImpl implements MessageService {

    private final IOService ioService;
    private final MessageSourceWrapper messageSourceWrapper;

    public MessageServiceImpl(IOService ioService,
                              MessageSourceWrapper messageSourceWrapper) {
        this.ioService = ioService;
        this.messageSourceWrapper = messageSourceWrapper;
    }

    @Override
    public void send(String code, Object... objects) {
        ioService.writeLine(messageSourceWrapper.getMessage(code, objects));
    }

    @Override
    public void sendNewLine(String code, Object... objects) {
        ioService.writeLine("\n" + messageSourceWrapper.getMessage(code, objects));
    }

    @Override
    public void sendNativeText(String message) {
        ioService.writeLine(message);
    }

    @Override
    public String sendWithRequest(String code, Object... objects) {
        return ioService.writeLineWithRequest(messageSourceWrapper.getMessage(code, objects) + ": ");
    }

    @Override
    public String sendNewLineWithRequest(String code, Object... objects) {
        return ioService.writeLineWithRequest("\n" + messageSourceWrapper.getMessage(code, objects) + ": ");
    }
}
