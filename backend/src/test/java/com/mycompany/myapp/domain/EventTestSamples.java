package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class EventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Event getEventSample1() {
        return new Event()
            .id(1L)
            .eventId(1L)
            .title("title1")
            .subtitle("subtitle1")
            .descripcion("descripcion1")
            .filas(1)
            .columnas(1)
            .tipoEvento("tipoEvento1");
    }

    public static Event getEventSample2() {
        return new Event()
            .id(2L)
            .eventId(2L)
            .title("title2")
            .subtitle("subtitle2")
            .descripcion("descripcion2")
            .filas(2)
            .columnas(2)
            .tipoEvento("tipoEvento2");
    }

    public static Event getEventRandomSampleGenerator() {
        return new Event()
            .id(longCount.incrementAndGet())
            .eventId(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .subtitle(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .filas(intCount.incrementAndGet())
            .columnas(intCount.incrementAndGet())
            .tipoEvento(UUID.randomUUID().toString());
    }
}
