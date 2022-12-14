package ru.otus.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import ru.otus.spring.service.TestingService;

@ComponentScan
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
public class App {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(App.class);
        TestingService testingService = context.getBean(TestingService.class);

        testingService.run();

        context.close();
    }
}
