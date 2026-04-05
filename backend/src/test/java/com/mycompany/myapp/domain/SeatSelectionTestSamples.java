package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SeatSelectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SeatSelection getSeatSelectionSample1() {
        return new SeatSelection().id(1L).eventId(1L).userLogin("userLogin1").asientos("asientos1");
    }

    public static SeatSelection getSeatSelectionSample2() {
        return new SeatSelection().id(2L).eventId(2L).userLogin("userLogin2").asientos("asientos2");
    }

    public static SeatSelection getSeatSelectionRandomSampleGenerator() {
        return new SeatSelection()
            .id(longCount.incrementAndGet())
            .eventId(longCount.incrementAndGet())
            .userLogin(UUID.randomUUID().toString())
            .asientos(UUID.randomUUID().toString());
    }
}
