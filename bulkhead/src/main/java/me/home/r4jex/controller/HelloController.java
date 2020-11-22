package me.home.r4jex.controller;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import lombok.AllArgsConstructor;
import me.home.r4jex.domain.BulkheadException;
import me.home.r4jex.domain.HelloService;
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
    public String hello(@RequestParam(value = "name", defaultValue = "joel") String name) {
        return helloService.hello(name);
    }

    @ExceptionHandler(BulkheadException.class)
    public ResponseEntity rateLimitHandler(BulkheadException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", ex.getRetryAfterSeconds())
                .body(ex.getMsg());
    }
}
