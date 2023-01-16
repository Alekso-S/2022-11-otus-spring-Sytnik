package ru.otus.spring.shell;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.service.TestingService;

@ShellComponent
public class Commands {

    private final TestingService testingService;

    public Commands(TestingService testingService) {
        this.testingService = testingService;
    }

    @ShellMethod(key = "name", value = "Set student name")
    public void setStudentName(String name) {
        testingService.setStudentName(name);
    }

    @ShellMethod(key = "start", value = "Start testing")
    @ShellMethodAvailability(value = "isTestingAvailable")
    public void startTesting() {
        testingService.run();
    }

    private Availability isTestingAvailable() {
        return testingService.checkTestingAvailability() ?
                Availability.available() : Availability.unavailable("");
    }
}
