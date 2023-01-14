package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.spring.service.TestingService;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ApplicationContext context =  SpringApplication.run(App.class);

        TestingService testingService = context.getBean(TestingService.class);

        testingService.run();
    }
}
