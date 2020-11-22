package me.home.r4jex.domain;

import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HelloService {

    @Bulkhead(name = "helloBulkhead", fallbackMethod = "helloFallback")
    public String hello(String name) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        return "hello " + name;
    }

    public String helloFallback(String name, BulkheadFullException ex) {
        log.info("Bulkhead are fulled no further calls will be accepted.");
        throw new BulkheadException("Bulkhead are fulled no further calls will be accepted.", "10");
    }
}
