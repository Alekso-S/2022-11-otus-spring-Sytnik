package ru.otus.spring.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.domain.Plastic;

@MessagingGateway
public interface ProductionGateway {

    String INPUT_CHANNEL = "inputChannel";

    @Gateway(requestChannel = INPUT_CHANNEL)
    void produce(Plastic plastic);
}