package ru.otus.spring.service;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootConfiguration
@EnableCircuitBreaker
@EnableAspectJAutoProxy
public class TestConfig {
}
