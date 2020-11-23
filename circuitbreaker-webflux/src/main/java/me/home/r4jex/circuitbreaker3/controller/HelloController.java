package me.home.r4jex.circuitbreaker3.controller;

import lombok.AllArgsConstructor;
import me.home.r4jex.circuitbreaker3.domain.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("namaste")
    public Mono<String> namaste(@RequestParam(value = "name", defaultValue = "Joel") String name) {
        return helloService.namaste(name);
    }

    @GetMapping("salaam")
    public Mono<String> salaam(@RequestParam(value = "name", defaultValue = "Joel") String name) {
        return helloService.salaam(name);
    }

    @GetMapping("success")
    public String success() {
        helloService.successAll();

        return "success";
    }
}
