package me.home.r4jex.circuitbreaker.controller;

import lombok.AllArgsConstructor;
import me.home.r4jex.circuitbreaker.domain.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("hello") // testing random error
    public String hello(@RequestParam(value = "name", defaultValue = "randomerror") String name) {
        return helloService.hello(name);
    }

    @GetMapping("bonjour") // testing random slow
    public String bonjour(@RequestParam(value = "name", defaultValue = "randomslow") String name) {
        return helloService.bonjour(name);
    }


}
