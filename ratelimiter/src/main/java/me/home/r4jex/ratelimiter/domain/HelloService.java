package me.home.r4jex.ratelimiter.domain;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HelloService {

    @RateLimiter(name = "helloRateLimiter", fallbackMethod = "helloFallback")
    public String hello(String name) {
        return "Hello, " + name;
    }

    public String helloFallback(String name, RequestNotPermitted ex) {
        log.info("Rate limit applied no further calls will be accepted in 1s");
        throw new RateLimitException("Rate limit applied no further calls will be accepted.", "1");
    }
}
