package me.home.r4jex.api.controller;

import me.home.r4jex.api.domain.BizService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/d")
public class BackendDController {
    private final BizService backendService;

    public BackendDController(@Qualifier("backendDService") BizService bizService) {
        this.backendService = bizService;
    }

    @GetMapping("failure")
    public String failure(){
        return backendService.failure();
    }

    @GetMapping("success")
    public String success(){
        return backendService.success();
    }

    @GetMapping("ignore")
    public String ignore(){
        return backendService.ignoreException();
    }

    @GetMapping("fallback")
    public String failureWithFallback(){
        return backendService.failureWithFallback();
    }

    @GetMapping("timeout-fallback")
    public String timeoutWithFallback() {
        return backendService.timeoutWithFallback();
    }
}
