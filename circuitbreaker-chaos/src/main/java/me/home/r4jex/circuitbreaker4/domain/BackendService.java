package me.home.r4jex.circuitbreaker4.domain;

import org.springframework.stereotype.Service;

@Service
public class BackendService {

    public String ping() {
        return "pong";
    }
}
