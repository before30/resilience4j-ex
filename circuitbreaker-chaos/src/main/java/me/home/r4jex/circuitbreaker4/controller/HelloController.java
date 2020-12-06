package me.home.r4jex.circuitbreaker4.controller;

import lombok.AllArgsConstructor;
import me.home.r4jex.circuitbreaker4.domain.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("ohayo")
    public String ohayo(@RequestParam(value = "name", defaultValue = "Joel") String name) {
        return helloService.ohayo(name);
    }
}
