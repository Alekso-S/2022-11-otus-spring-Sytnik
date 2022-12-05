package ru.otus.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.io.IOService;
import ru.otus.spring.io.IOServiceConsole;

import java.io.InputStream;
import java.io.PrintStream;

@Configuration
public class TestingConfig {

    @Bean
    public IOService ioService() {
        return new IOServiceConsole(inputStream(), printStream());
    }

    public InputStream inputStream() {
        return System.in;
    }

    public PrintStream printStream () {
        return System.out;
    }
}
