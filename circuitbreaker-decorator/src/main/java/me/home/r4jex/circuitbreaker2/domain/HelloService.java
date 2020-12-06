package me.home.r4jex.circuitbreaker2.domain;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@Slf4j
public class HelloService {

    private final CircuitBreaker holaCircuit;
    private final Bulkhead holaBulkhead;
    private final CircuitBreaker nihauCircuit;
    private final Bulkhead nihauBulkhead;
    private final RestTemplate restTemplate;

    public HelloService(final CircuitBreakerRegistry circuitBreakerRegistry,
                        final BulkheadRegistry bulkheadRegistry,
                        final RestTemplateBuilder restTemplateBuilder) {
        CircuitBreakerConfig holaCircuitConfig = CircuitBreakerConfig.custom()
                .slidingWindow(100, 5, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .permittedNumberOfCallsInHalfOpenState(30)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .failureRateThreshold(30)
                .recordExceptions(HttpServerErrorException.class)
                .build();
        this.holaCircuit = circuitBreakerRegistry.circuitBreaker("hola_circuit", holaCircuitConfig);
        this.holaBulkhead = bulkheadRegistry.bulkhead("hola_bulkhead");

        CircuitBreakerConfig nihauCircuitConfig = CircuitBreakerConfig.custom()
                .slidingWindow(100, 5, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .permittedNumberOfCallsInHalfOpenState(10)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .failureRateThreshold(80)
                .recordExceptions(HttpServerErrorException.class)
                .build();
        this.nihauCircuit = circuitBreakerRegistry.circuitBreaker("nihau_circuit", nihauCircuitConfig);
        this.nihauBulkhead = bulkheadRegistry.bulkhead("nihau_bulkhead");

        restTemplate = restTemplateBuilder.build();
    }

    public String hola(String name) {

        return Decorators.ofSupplier(() -> doHola(name))
                .withBulkhead(holaBulkhead)
                .withCircuitBreaker(holaCircuit)
                .withFallback(this::holaFallback)
                .get();
    }

    private String doHola(String name) {
        return restTemplate.getForObject("http://localhost:8080/api/randomError", String.class);
    }

    private String holaFallback(Throwable ex) {
        log.info("hola fallback. {}", ex.getMessage());
        return "hola fallback";
    }

    public String nihau(String name) {
        Supplier<String> decoratedSupplier = CircuitBreaker
                .decorateSupplier(nihauCircuit, () -> {
                    return restTemplate.getForObject("http://localhost:8080/api/randomError", String.class);
                });
        return Try.ofSupplier(decoratedSupplier)
                .recover(this::nihauFallback)
                .get();
    }

    private String nihauFallback(Throwable ex) {
        log.info("nihau fallback. {}", ex.getMessage());
        return "nihau fallback";
    }

    public String holaSuccess() {
        return Decorators.ofSupplier(() -> "success")
                .withCircuitBreaker(holaCircuit)
                .get();
    }

    public String nihauSuccess() {
        return "success";
    }
}
