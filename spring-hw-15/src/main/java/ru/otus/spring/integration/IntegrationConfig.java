package ru.otus.spring.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.spring.domain.Plastic;
import ru.otus.spring.enums.Assignment;
import ru.otus.spring.service.BottleKitService;
import ru.otus.spring.service.BottleService;
import ru.otus.spring.service.CapService;
import ru.otus.spring.service.PlasticService;

import java.util.List;
import java.util.concurrent.Executors;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    private final PlasticService plasticService;
    private final BottleService bottleService;
    private final CapService capService;
    private final BottleKitService bottleKitService;

    private final static String CORRELATION_KEY = "order";

    @Bean
    public IntegrationFlow acceptanceFlow() {
        return IntegrationFlows
                .from(ProductionGateway.INPUT_CHANNEL)
                .split(Plastic.class, plastic -> List.of(
                        plasticService.getBottleVolume(plastic),
                        plasticService.getCapVolume(plastic)
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
                .filter(bottleService::check, f -> f.poller(getBottlePoller()))
                .enrichHeaders(h -> h.headerFunction(CORRELATION_KEY, m -> bottleService.getCnt()))
                .log(message -> "Bottles produced: " + bottleService.getCnt())
                .channel("assemblyFlow.input");
    }

    public IntegrationFlow capFlow() {
        return flow -> flow
                .split(Plastic.class, capService::produce)
                .channel(channels -> channels.queue())
                .filter(capService::check, f -> f.poller(getCapPoller()))
                .enrichHeaders(h -> h.headerFunction(CORRELATION_KEY, m -> capService.getCnt()))
                .log(message -> "Caps produced: " + capService.getCnt())
                .channel("assemblyFlow.input");
    }

    @Bean
    public IntegrationFlow assemblyFlow() {
        return flow -> flow
                .aggregate(aggregatorSpec -> aggregatorSpec
                        .correlationStrategy(message -> message
                                .getHeaders().get(CORRELATION_KEY))
                        .releaseStrategy(group -> group.size() == 2)
                )
                .log(message -> "Bottle kits produced: " + bottleKitService.incrementAndGet());
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
