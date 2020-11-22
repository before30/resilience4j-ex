package me.home.r4jex.circuitbreaker.controller;

import lombok.AllArgsConstructor;
import me.home.r4jex.circuitbreaker.domain.GreetingService;
import me.home.r4jex.circuitbreaker.domain.GreetingRandomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class GreetingController {

    private final GreetingService greetingService;

    @GetMapping("randomError")
    public String randomError() {
        return greetingService.randomError();
    }

    @GetMapping("randomSlow")
    public String randomSlow() {
        return greetingService.randomSlow();
    }

    @ExceptionHandler(GreetingRandomException.class)
    public ResponseEntity randomErrorHandler(GreetingRandomException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
