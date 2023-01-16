package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;

@Service
public class StudentServiceImpl implements StudentService {

    private final MessageService messageService;

    public StudentServiceImpl(MessageService messageService) {
        this.messageService = messageService;
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
        return messageService.SendWithRequest("student-name-request");
    }

    @Override
    public Student getStudentByName(String fullName) {
        if (!fullName.matches(".+ .+")) {
            messageService.Send("student-name-request");
            return null;
        }

        messageService.Send("student-name-set");
        return new Student(fullName.split(" ")[0], fullName.split(" ")[1]);
    }
}
