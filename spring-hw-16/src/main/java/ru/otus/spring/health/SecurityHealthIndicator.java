package ru.otus.spring.health;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Component
@Getter(onMethod_={@Synchronized})
@Setter(onMethod_={@Synchronized})
public class SecurityHealthIndicator implements HealthIndicator {

    private final static int CALM_DOWN_TIME_SEC = 60;

    private String user = null;
    private String method = null;
    private String url = null;
    private LocalDateTime dateTime = LocalDateTime.MIN;

    @Override
    public Health health() {
        if (getDateTime().until(LocalDateTime.now(), ChronoUnit.SECONDS) <= CALM_DOWN_TIME_SEC) {
            return Health.down()
                    .withDetails(Map.of(
                            "user", getUser(),
                            "method", getMethod(),
                            "url", getUrl()))
                    .build();
        } else {
            return Health.up()
                    .build();
        }
    }

    public void registerAccessAttempt(HttpServletRequest request) {
        setUser(request.getRemoteUser());
        setMethod(request.getMethod());
        setUrl(request.getRequestURI());
        setDateTime(LocalDateTime.now());
    }
}
