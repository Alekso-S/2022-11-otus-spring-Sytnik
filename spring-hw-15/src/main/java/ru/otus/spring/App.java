package ru.otus.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.spring.domain.Plastic;
import ru.otus.spring.integration.ProductionGateway;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(App.class, args);

        var gateway = context.getBean(ProductionGateway.class);

        gateway.produce(new Plastic(1000));
    }
}
