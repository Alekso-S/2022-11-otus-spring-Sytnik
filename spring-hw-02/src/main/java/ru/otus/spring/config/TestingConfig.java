package ru.otus.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.io.IOService;
import ru.otus.spring.io.IOServiceStream;

@Configuration
public class TestingConfig {

    @Bean
    public IOService ioService() {
        return new IOServiceStream(System.in, System.out);
    }
}
