package me.home.r4jex.circuitbreaker.domain;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class HelloService {
    private final RestTemplate restTemplate;
    private String helloCache = "";
    private String bonjourCache = "";

    public HelloService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    @CircuitBreaker(name = "helloCircuit", fallbackMethod = "helloFallback")
    public String hello(String name) {
        helloCache = restTemplate.getForObject("http://localhost:8080/api/randomError", String.class);
        return helloCache;
    }

    public String helloFallback(String name, CallNotPermittedException ex) {
        log.info("Hello Circuit(Error) is in open state. it's fallback.");
        return helloCache;
    }

    @CircuitBreaker(name = "helloCircuit")
    public String helloSuccess() {
        return "success";
    }

    @CircuitBreaker(name = "bonjourCircuit", fallbackMethod = "bonjourFallback")
    public String bonjour(String name) {
        bonjourCache = restTemplate.getForObject("http://localhost:8080/api/randomSlow", String.class);
        return bonjourCache;
    }

    public String bonjourFallback(String name, CallNotPermittedException ex) {
        log.info("Bonjour Circuit(slow) is in open state. it's fallback.");
        return bonjourCache;
    }

    @CircuitBreaker(name = "bonjourCircuit")
    public String bonjourSuccess() {
        return "success";
    }
}
