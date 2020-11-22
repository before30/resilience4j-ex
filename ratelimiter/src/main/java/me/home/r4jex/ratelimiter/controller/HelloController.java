package me.home.r4jex.ratelimiter.controller;

import lombok.AllArgsConstructor;
import me.home.r4jex.ratelimiter.domain.HelloService;
import me.home.r4jex.ratelimiter.domain.RateLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HelloController {
    private final HelloService helloService;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "Joel") String name) {
        return helloService.hello(name);
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity rateLimitHandler(RateLimitException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", ex.getRetryAfterSeconds())
                .body(ex.getMsg());
    }
}
