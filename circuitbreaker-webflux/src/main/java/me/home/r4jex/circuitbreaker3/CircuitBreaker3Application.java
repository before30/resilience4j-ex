package me.home.r4jex.circuitbreaker3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class CircuitBreaker3Application {
    public static void main(String[] args) {
        SpringApplication.run(CircuitBreaker3Application.class, args);
    }
}
