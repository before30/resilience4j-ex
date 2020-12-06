package me.home.r4jex.circuitbreaker4.controller;

import lombok.AllArgsConstructor;
import me.home.r4jex.circuitbreaker4.domain.BackendService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BackendController {

    private BackendService backendService;

    @GetMapping("/ping")
    public String ping() {
        return backendService.ping();
    }
}
