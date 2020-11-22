package me.home.r4jex.api.domain;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkhead;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import me.home.r4jex.api.domain.exception.BizException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@Slf4j
public class BackendAService implements BizService {
    private final String SERVICE_NAME = "BackendA";
    private final CircuitBreaker circuitBreaker;
    private final Bulkhead bulkhead;
    private final TimeLimiter timeLimiter;
    private final ThreadPoolBulkhead threadPoolBulkhead;

    public BackendAService(final CircuitBreakerRegistry circuitBreakerRegistry,
                           final BulkheadRegistry bulkheadRegistry,
                           final TimeLimiterRegistry timeLimiterRegistry,
                           final ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry) {
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker(SERVICE_NAME + ".circuit");
        this.bulkhead = bulkheadRegistry.bulkhead(SERVICE_NAME + ".bulkhead");
        this.timeLimiter = timeLimiterRegistry.timeLimiter(SERVICE_NAME + ".timelimiter");
        this.threadPoolBulkhead = threadPoolBulkheadRegistry.bulkhead(SERVICE_NAME + ".bulkhead");
    }

    @Override
    public String failure() {
        return Decorators.ofSupplier(this::doFailure)
                .withBulkhead(bulkhead)
                .withCircuitBreaker(circuitBreaker)
                .get();
    }

    private String doFailure() {
        throw new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "This is a remote exception");
    }

    @Override
    public String failureWithFallback() {
        return Decorators.ofSupplier(this::doFailureWithFallback)
                .withBulkhead(bulkhead)
                .withCircuitBreaker(circuitBreaker)
                .withFallback(this::fallback)
                .get();
    }

    private String doFailureWithFallback() {
        return doFailure();
    }

    private String fallback(Throwable ex) {
        return "Recovered: " + ex.getMessage();
    }

    @Override
    public String success() {
        return Decorators.ofSupplier(this::doSuccess)
                .withCircuitBreaker(circuitBreaker)
                .withBulkhead(bulkhead)
                .get();
    }

    private String doSuccess() {
        return "Hello from backend A";
    }

    @Override
    public String successException() {
        return Decorators.ofSupplier(this::doSuccessException)
                .withCircuitBreaker(circuitBreaker)
                .withBulkhead(bulkhead)
                .get();
    }

    private String doSuccessException() {
        throw new HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "This is a remote client exception");
    }

    @Override
    public String ignoreException() {
        return Decorators.ofSupplier(this::doIgnoreException)
                .withCircuitBreaker(circuitBreaker)
                .withBulkhead(bulkhead)
                .get();
    }

    private String doIgnoreException() {
        throw new BizException("This exception is ignored by the CircuitBreaker of backend A");
    }

    @Override
    public String timeoutWithFallback() {
        return null;
    }
}
