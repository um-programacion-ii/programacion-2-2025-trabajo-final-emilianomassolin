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
            .titulo("titulo1")
            .resumen("resumen1")
            .descripcion("descripcion1")
            .filaAsientos(1)
            .columnaAsientos(1)
            .tipoEvento("tipoEvento1")
            .direccion("direccion1")
            .imagen("imagen1")
            .tipoNombre("tipoNombre1")
            .tipoDescripcion("tipoDescripcion1")
            .integrantes("integrantes1");
    }

    public static Event getEventSample2() {
        return new Event()
            .id(2L)
            .eventId(2L)
            .titulo("titulo2")
            .resumen("resumen2")
            .descripcion("descripcion2")
            .filaAsientos(2)
            .columnaAsientos(2)
            .tipoEvento("tipoEvento2")
            .direccion("direccion2")
            .imagen("imagen2")
            .tipoNombre("tipoNombre2")
            .tipoDescripcion("tipoDescripcion2")
            .integrantes("integrantes2");
    }

    public static Event getEventRandomSampleGenerator() {
        return new Event()
            .id(longCount.incrementAndGet())
            .eventId(longCount.incrementAndGet())
            .titulo(UUID.randomUUID().toString())
            .resumen(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString())
            .filaAsientos(intCount.incrementAndGet())
            .columnaAsientos(intCount.incrementAndGet())
            .tipoEvento(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString())
            .imagen(UUID.randomUUID().toString())
            .tipoNombre(UUID.randomUUID().toString())
            .tipoDescripcion(UUID.randomUUID().toString())
            .integrantes(UUID.randomUUID().toString());
    }
}
