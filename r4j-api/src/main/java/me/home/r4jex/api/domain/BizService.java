package me.home.r4jex.api.domain;

public interface BizService {

    String failure();

    String failureWithFallback();

    String success();

    String successException();

    String ignoreException();

    String timeoutWithFallback();
}
