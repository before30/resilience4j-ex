package me.home.r4jex.circuitbreaker3.domain;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Slf4j
public class HelloService {

    private final CircuitBreaker namasteCircuit;
    private final CircuitBreaker salaamCircuit;
    private final RestTemplate restTemplate;

    public HelloService(final RestTemplateBuilder restTemplateBuilder,
                        final CircuitBreakerRegistry circuitBreakerRegistry) {

        CircuitBreakerConfig circuitConfig = CircuitBreakerConfig.custom()
                .slidingWindow(100, 5, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .permittedNumberOfCallsInHalfOpenState(30)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .failureRateThreshold(30)
                .recordExceptions(HttpServerErrorException.class,
                        WebClientResponseException.class)
                .build();

        namasteCircuit = circuitBreakerRegistry.circuitBreaker("namaste_circuit", circuitConfig);
        salaamCircuit = circuitBreakerRegistry.circuitBreaker("salaam_circuit", circuitConfig);
        restTemplate = restTemplateBuilder.build();
    }

    public Mono<String> namaste(String name) {
        return Mono.fromCallable(() -> restTemplate.getForObject("http://localhost:8080/api/randomError", String.class))
                .transformDeferred(CircuitBreakerOperator.of(namasteCircuit))
                .onErrorResume(this::namasteFallback);
    }

    public Mono<String> namasteFallback(Throwable throwable) {
        log.info("namaste fallback {}", throwable.getMessage());
        return Mono.just("fallback");
    }

    public Mono<String> salaam(String name) {
        Mono<String> response = WebClient.create("http://localhost:8080")
                .get().uri("/api/randomError").retrieve().bodyToMono(String.class);

        return response.transformDeferred(CircuitBreakerOperator.of(salaamCircuit))
                .onErrorResume(this::salaamFallback);
    }

    public Mono<String> salaamFallback(Throwable throwable) {
        log.info("sallam fallback {}", throwable.getMessage());
        return Mono.just("fallback");
    }

    public void successAll() {
        namasteCircuit.transitionToClosedState();
        salaamCircuit.transitionToClosedState();
    }
}
