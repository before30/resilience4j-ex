package me.home.r4jex.circuitbreaker.domain;

public class RandomException extends RuntimeException {
    public RandomException(String msg) {
        super(msg);
    }
}
