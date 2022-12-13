package ru.otus.spring.service;

import org.springframework.stereotype.Service;
import ru.otus.spring.domain.Student;

@Service
public class StudentServiceImpl implements StudentService {

    private final PrinterService printerService;

    public StudentServiceImpl(PrinterService printerService) {
        this.printerService = printerService;
    }

    @Override
    public Student requestStudent() {
        String fullName;

        do {
            fullName = printerService.askForName();
        } while (!fullName.matches(".+ .+"));

        return new Student(fullName.split(" ")[0], fullName.split(" ")[1]);
    }
}
