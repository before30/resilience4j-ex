package me.home.r4jex.circuitbreaker.domain;

public class GreetingRandomException extends RuntimeException {
    public GreetingRandomException(String msg) {
        super(msg);
    }
}
