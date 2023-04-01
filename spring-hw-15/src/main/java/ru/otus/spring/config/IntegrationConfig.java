package ru.otus.spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.spring.domain.Plastic;
import ru.otus.spring.enums.Assignment;
import ru.otus.spring.service.BottleService;
import ru.otus.spring.service.CapService;
import ru.otus.spring.service.QualityService;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@IntegrationComponentScan
@RequiredArgsConstructor
public class IntegrationConfig {

    private final BottleService bottleService;
    private final CapService capService;
    private final QualityService qualityService;

    private final AtomicInteger bottleCounter = new AtomicInteger();
    private final AtomicInteger capCounter = new AtomicInteger();
    private final AtomicInteger bottleKitCounter = new AtomicInteger();

    @MessagingGateway
    public interface TestGateway {

        @Gateway(requestChannel = "inputChannel")
        void produce(Plastic plastic);
    }

    @Bean
    public IntegrationFlow acceptanceFlow() {
        return IntegrationFlows
                .from("inputChannel")
                .split(Plastic.class, plastic -> List.of(
                        new Plastic(Assignment.BOTTLE, plastic.getVolume() / 13 * 12),
                        new Plastic(Assignment.CAP, plastic.getVolume() / 13)
                ))
                .channel(c -> c.executor(Executors.newCachedThreadPool()))
                .<Plastic, Assignment>route(Plastic::getAssignment, mapping -> mapping
                        .subFlowMapping(Assignment.BOTTLE, bottleFlow())
                        .subFlowMapping(Assignment.CAP, capFlow())
                )
                .get();
    }

    public IntegrationFlow bottleFlow() {
        return flow -> flow
                .split(Plastic.class, bottleService::produce)
                .channel(channels -> channels.queue())
                .filter(qualityService::check, f -> f.poller(getBottlePoller()))
                .enrichHeaders(h -> h.headerFunction("order", m -> bottleCounter.incrementAndGet()))
                .log(message -> "Bottles produced: " + bottleCounter.get())
                .channel("assemblyFlow.input");
    }

    public IntegrationFlow capFlow() {
        return flow -> flow
                .split(Plastic.class, capService::produce)
                .channel(channels -> channels.queue())
                .filter(qualityService::check, f -> f.poller(getCapPoller()))
                .enrichHeaders(h -> h.headerFunction("order", m -> capCounter.incrementAndGet()))
                .log(message -> "Caps produced: " + capCounter.get())
                .channel("assemblyFlow.input");
    }

    @Bean
    public IntegrationFlow assemblyFlow() {
        return flow -> flow
                .aggregate(aggregatorSpec -> aggregatorSpec
                        .correlationStrategy(message -> message
                                .getHeaders().get("order"))
                        .releaseStrategy(group -> group.size() == 2)
                )
                .log(message -> "Bottle kits produced: " + bottleKitCounter.incrementAndGet());
    }

    public PollerMetadata getBottlePoller() {
        return Pollers.fixedRate(300)
                .maxMessagesPerPoll(1)
                .taskExecutor(Executors.newSingleThreadExecutor())
                .get();
    }

    public PollerMetadata getCapPoller() {
        return Pollers.fixedRate(400)
                .maxMessagesPerPoll(2)
                .taskExecutor(Executors.newSingleThreadExecutor())
                .get();
    }
}
