package me.home.r4jex.api.config;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.bulkhead.ThreadPoolBulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedBulkheadMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedThreadPoolBulkheadMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedTimeLimiterMetrics;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import me.home.r4jex.api.domain.exception.BizException;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class CircuitConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(MeterRegistry meterRegistry) {
        CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig.custom()
                        .slidingWindow(10, 5, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                        // Circuit Type 어떤 곳에 쓰면 좋을지
                        .failureRateThreshold(50)
                        .ignoreExceptions(BizException.class)
                        .build();

        CircuitBreakerRegistry circuitBreakerRegistry= CircuitBreakerRegistry.of(circuitBreakerConfig);
        TaggedCircuitBreakerMetrics.ofCircuitBreakerRegistry(circuitBreakerRegistry)
                .bindTo(meterRegistry);

        return circuitBreakerRegistry;
    }

    @Bean
    public BulkheadRegistry bulkheadRegistry(MeterRegistry meterRegistry) {
        BulkheadRegistry bulkheadRegistry = BulkheadRegistry.ofDefaults();
        TaggedBulkheadMetrics.ofBulkheadRegistry(bulkheadRegistry)
                .bindTo(meterRegistry);

        return bulkheadRegistry;
    }

    @Bean
    public TimeLimiterRegistry timeLimiterRegistry(MeterRegistry meterRegistry) {
        TimeLimiterRegistry timeLimiterRegistry = TimeLimiterRegistry.ofDefaults();
        TaggedTimeLimiterMetrics.ofTimeLimiterRegistry(timeLimiterRegistry)
                .bindTo(meterRegistry);

        return timeLimiterRegistry;
    }

    @Bean
    public ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry(MeterRegistry meterRegistry) {
        ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry = ThreadPoolBulkheadRegistry.ofDefaults();
        TaggedThreadPoolBulkheadMetrics.ofThreadPoolBulkheadRegistry(threadPoolBulkheadRegistry)
                .bindTo(meterRegistry);

        return threadPoolBulkheadRegistry;
    }

    @Bean
    public CircuitBreakerFactory circuitBreakerFactory() {
        Resilience4JCircuitBreakerFactory factory = new Resilience4JCircuitBreakerFactory();
        factory.configureDefault(new Function<String, Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration>() {
            @Override
            public Resilience4JConfigBuilder.Resilience4JCircuitBreakerConfiguration apply(String s) {
                return new Resilience4JConfigBuilder(s)
                        .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                        .build();
            }
        });
        return factory;
    }
}
