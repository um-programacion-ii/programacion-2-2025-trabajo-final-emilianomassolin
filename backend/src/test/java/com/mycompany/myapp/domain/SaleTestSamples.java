package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class SaleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Sale getSaleSample1() {
        return new Sale()
            .id(1L)
            .ventaId(1L)
            .eventId(1L)
            .asientos("asientos1")
            .nombres("nombres1")
            .estado("estado1")
            .descripcion("descripcion1")
            .cantidadAsientos(1);
    }

    public static Sale getSaleSample2() {
        return new Sale()
            .id(2L)
            .ventaId(2L)
            .eventId(2L)
            .asientos("asientos2")
            .nombres("nombres2")
            .estado("estado2")
            .descripcion("descripcion2")
            .cantidadAsientos(2);
    }

    public static Sale getSaleRandomSampleGenerator() {
        return new Sale()
            .id(longCount.incrementAndGet())
            .ventaId(longCount.incrementAndGet())
            .eventId(longCount.incrementAndGet())
            .asientos(UUID.randomUUID().toString())
            .nombres(UUID.randomUUID().toString())
            .estado(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .cantidadAsientos(intCount.incrementAndGet());
    }
}
