package me.home.r4jex.circuitbreaker2.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class BackendService {
    private final Random random = new Random();

    public String randomError() {
        int i = random.nextInt(2);

        if (i == 0) {
            log.info("Greeting Service will generate exception.");
            throw new RandomException("Random Exception.");
        }
        return "Good Morning.";
    }

    public String randomSlow() {
        int i = random.nextInt(2);

        if (i == 0) {
            log.info("Greeting Service takes 2 secs");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }

        return "Good Afternoon.";
    }
}
