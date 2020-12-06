package me.home.r4jex.circuitbreaker4.domain;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Service
@Slf4j
public class HelloService {
    private final CircuitBreaker ohayoCircuit;
    private final RestTemplate restTemplate;

    public HelloService(final CircuitBreakerRegistry circuitBreakerRegistry,
                        final RestTemplateBuilder restTemplateBuilder) {
        CircuitBreakerConfig holaCircuitConfig = CircuitBreakerConfig.custom()
                .slidingWindow(100, 5, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .permittedNumberOfCallsInHalfOpenState(10)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .slowCallDurationThreshold(Duration.ofMillis(500))
                .slowCallRateThreshold(30)
                .failureRateThreshold(30)
                .recordExceptions(HttpServerErrorException.class)
                .build();
        this.ohayoCircuit = circuitBreakerRegistry.circuitBreaker("hola_circuit", holaCircuitConfig);
        restTemplate = restTemplateBuilder.build();
    }

    public String ohayo(String name) {

        return Decorators.ofSupplier(() -> doOhayo(name))
                .withCircuitBreaker(ohayoCircuit)
                .withFallback(this::ohayoFallback)
                .get();
    }

    private String doOhayo(String name) {
        return restTemplate.getForObject("http://localhost:8080/api/ping", String.class);
    }

    private String ohayoFallback(Throwable ex) {
        log.info("ohayo fallback. {}", ex.getMessage());
        return "Fall Back.";
    }
}
