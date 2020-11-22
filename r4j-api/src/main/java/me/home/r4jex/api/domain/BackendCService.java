package me.home.r4jex.api.domain;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import me.home.r4jex.api.domain.exception.BizException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@Slf4j
public class BackendCService implements BizService {

    private final String SERVICE_NAME = "BackendC";
    private final String CIRCUIT_BREAKER_NAME = SERVICE_NAME +".circuit";
    private final String BULKHEAD_NAME = SERVICE_NAME + ".bulkhead";

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME)
    @Bulkhead(name = BULKHEAD_NAME)
    public String failure() {
        throw new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "This is a remote exception");
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallback")
    @Bulkhead(name = BULKHEAD_NAME)
    public String failureWithFallback() {
        return failure();
    }

    private String fallback(Throwable ex) {
        return "Recovered: " + ex.getMessage();
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME)
    @Bulkhead(name = BULKHEAD_NAME)
    public String success() {
        return "Hello from backend B";
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME)
    @Bulkhead(name = BULKHEAD_NAME)
    public String successException() {
        throw new HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "This is a remote client exception");
    }

    @Override
    @CircuitBreaker(name = CIRCUIT_BREAKER_NAME)
    @Bulkhead(name = BULKHEAD_NAME)
    public String ignoreException() {
        throw new BizException("This exception is ignored by the CircuitBreaker of backend C");
    }

    @Override
    public String timeoutWithFallback() {
        return null;
    }
}
