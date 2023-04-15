package ru.otus.spring.health;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.spring.repository.BookRepository;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ContentHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;

    private final static int BOOKS_COUNT_LIMIT = 8;

    @Override
    public Health health() {
        long booksCnt = bookRepository.count();
        return booksCnt < BOOKS_COUNT_LIMIT ?
                Health.down()
                        .withDetails(Map.of("books count", booksCnt))
                        .build() :
                Health.up().build();
    }
}
