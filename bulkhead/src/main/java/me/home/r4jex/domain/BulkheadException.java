package me.home.r4jex.domain;

import lombok.Getter;

@Getter
public class BulkheadException extends RuntimeException {

    private String msg;
    private String retryAfterSeconds;

    public BulkheadException(String msg, String retryAfterSeconds) {
        super(msg);
        this.msg = msg;
        this.retryAfterSeconds = retryAfterSeconds;
    }
}
