package me.home.r4jex.ratelimiter.domain;

import lombok.Getter;

@Getter
public class RateLimitException extends RuntimeException {

    private String msg;
    private String retryAfterSeconds;

    public RateLimitException(String msg, String retryAfterSeconds) {
        super(msg);
        this.msg = msg;
        this.retryAfterSeconds = retryAfterSeconds;
    }
}
