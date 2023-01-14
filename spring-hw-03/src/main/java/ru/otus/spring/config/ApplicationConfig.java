package ru.otus.spring.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.io.IOService;
import ru.otus.spring.io.IOServiceStream;

@Configuration
@EnableConfigurationProperties(AppProps.class)
public class ApplicationConfig {

    @Bean
    public IOService ioService() {
        return new IOServiceStream(System.in, System.out);
    }
}
