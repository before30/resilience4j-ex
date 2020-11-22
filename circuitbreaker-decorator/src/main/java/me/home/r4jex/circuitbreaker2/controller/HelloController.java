package me.home.r4jex.circuitbreaker2.controller;

import lombok.AllArgsConstructor;
import me.home.r4jex.circuitbreaker2.domain.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("hola")
    public String hola(@RequestParam(value = "name", defaultValue = "Joel") String name) {
        return helloService.hola(name);
    }

    @GetMapping("nihau")
    public String nihau(@RequestParam(value = "name", defaultValue = "Joel") String name) {
        return helloService.nihau(name);
    }

    @GetMapping("success")
    public String success() {
        helloService.holaSuccess();
        helloService.nihauSuccess();

        return "success";
    }
}
