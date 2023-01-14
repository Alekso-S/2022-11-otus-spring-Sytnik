package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;
import ru.otus.spring.io.IOService;
import ru.otus.spring.wrapper.MessageSourceWrapper;

@Service
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;
    private final MessageSourceWrapper messageSourceWrapper;

    public StudentServiceImpl(IOService ioService,
                              MessageSourceWrapper messageSourceWrapper) {
        this.ioService = ioService;
        this.messageSourceWrapper = messageSourceWrapper;
    }

    @Override
    public Student requestStudent() {
        String fullName;

        do {
            fullName = askForName();
        } while (!fullName.matches(".+ .+"));

        return new Student(fullName.split(" ")[0], fullName.split(" ")[1]);
    }

    private String askForName() {
        return ioService.readLineWithRequest(messageSourceWrapper.getMessage("student-name-request") + ": ");
    }
}
