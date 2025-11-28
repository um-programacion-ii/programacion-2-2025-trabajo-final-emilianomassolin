package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserSessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserSession getUserSessionSample1() {
        return new UserSession().id(1L).userLogin("userLogin1").step(1).eventId(1L).asientos("asientos1").nombres("nombres1");
    }

    public static UserSession getUserSessionSample2() {
        return new UserSession().id(2L).userLogin("userLogin2").step(2).eventId(2L).asientos("asientos2").nombres("nombres2");
    }

    public static UserSession getUserSessionRandomSampleGenerator() {
        return new UserSession()
            .id(longCount.incrementAndGet())
            .userLogin(UUID.randomUUID().toString())
            .step(intCount.incrementAndGet())
            .eventId(longCount.incrementAndGet())
            .asientos(UUID.randomUUID().toString())
            .nombres(UUID.randomUUID().toString());
    }
}
