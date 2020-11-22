package me.home.r4jex.api.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BackendDService implements BizService {

    private final String SERVICE_NAME = "BackendD";
    private final String CIRCUIT_BREAKER_NAME = SERVICE_NAME +".circuit";

    private final CircuitBreaker circuitBreaker;

    public BackendDService(CircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreaker = circuitBreakerFactory.create(CIRCUIT_BREAKER_NAME);
    }

    @Override
    public String failure() {
        // Error로 서킷 여는 것과
        // 시간으로 여는 것 2가
        return null;
    }

    @Override
    public String failureWithFallback() {
        return null;
    }

    @Override
    public String success() {
        return null;
    }

    @Override
    public String successException() {
        return null;
    }

    @Override
    public String ignoreException() {
        return null;
    }

    @Override
    public String timeoutWithFallback() {
        return null;
    }
}
