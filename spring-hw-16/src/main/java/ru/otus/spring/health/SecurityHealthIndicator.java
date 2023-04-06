package ru.otus.spring.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Component
public class SecurityHealthIndicator implements HealthIndicator {

    private final static int CALM_DOWN_TIME_SEC = 60;

    private String user = null;
    private String method = null;
    private String url = null;
    private LocalDateTime dateTime = LocalDateTime.MIN;

    @Override
    public Health health() {
        if (dateTime.until(LocalDateTime.now(), ChronoUnit.SECONDS) <= CALM_DOWN_TIME_SEC) {
            return Health.down()
                    .withDetails(Map.of(
                            "user", user,
                            "method", method,
                            "url", url))
                    .build();
        } else {
            return Health.up()
                    .build();
        }
    }

    public void registerAccessAttempt(HttpServletRequest request) {
        user = request.getRemoteUser();
        method = request.getMethod();
        url = request.getRequestURI();
        dateTime = LocalDateTime.now();
    }
}
