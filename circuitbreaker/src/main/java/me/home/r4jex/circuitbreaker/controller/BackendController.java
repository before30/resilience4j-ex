package me.home.r4jex.circuitbreaker.controller;

import lombok.AllArgsConstructor;
import me.home.r4jex.circuitbreaker.domain.BackendService;
import me.home.r4jex.circuitbreaker.domain.RandomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BackendController {

    private final BackendService backendService;

    @GetMapping("randomError")
    public String randomError() {
        return backendService.randomError();
    }

    @GetMapping("randomSlow")
    public String randomSlow() {
        return backendService.randomSlow();
    }

    @ExceptionHandler(RandomException.class)
    public ResponseEntity randomErrorHandler(RandomException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
