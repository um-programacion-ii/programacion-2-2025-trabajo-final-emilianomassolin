package com.mycompany.proxy.service;

import com.mycompany.proxy.service.dto.SeatDTO;
import com.mycompany.proxy.service.dto.SeatMapDTO;
import com.mycompany.proxy.service.dto.SeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatMapService {

    private final StringRedisTemplate redisTemplate;
    private final String seatKeyPrefix;

    public SeatMapService(StringRedisTemplate redisTemplate,
                          @Value("${proxy.redis.seat-key-prefix:evento}") String seatKeyPrefix) {
        this.redisTemplate = redisTemplate;
        this.seatKeyPrefix = seatKeyPrefix;
    }

    /**
     * Devuelve el mapa de asientos para un evento.
     *
     * Por ahora:
     *  - genera una grilla MOCK (10x20) con estados pseudo-aleatorios.
     *  - Más adelante se cambia la lógica para leer desde Redis de la cátedra.
     */
    public SeatMapDTO getSeatMapForEvent(Long eventoId) {
        // TODO: cuando conozcamos el formato real de Redis de la cátedra,
        //       leer filas, columnas y estados desde allí.
        int filas = 10;
        int columnas = 20;

        List<SeatDTO> asientos = new ArrayList<>();
        for (int f = 1; f <= filas; f++) {
            for (int c = 1; c <= columnas; c++) {
                SeatStatus status = loadSeatStatusFromRedis(eventoId, f, c);
                asientos.add(new SeatDTO(f, c, status));
            }
        }

        return new SeatMapDTO(eventoId, filas, columnas, asientos);
    }

    /**
     * Lógica para consultar Redis.
     * De momento devuelve un MOCK:
     *   - algunos ocupados, otros bloqueados, el resto libres.
     */
    private SeatStatus loadSeatStatusFromRedis(Long eventoId, int fila, int columna) {
        // Ejemplo de posible clave:
        //  key = "<prefix>:<eventoId>:<fila>:<columna>"
        String key = seatKeyPrefix + ":" + eventoId + ":" + fila + ":" + columna;

        // Cuando sepas el formato real:
        // String value = redisTemplate.opsForValue().get(key);
        // if ("LIBRE".equalsIgnoreCase(value)) return SeatStatus.LIBRE; ...

        // MOCK deterministic (para poder probar la UI):
        if ((fila + columna) % 7 == 0) {
            return SeatStatus.OCUPADO;
        }
        if ((fila + columna) % 5 == 0) {
            return SeatStatus.BLOQUEADO;
        }
        return SeatStatus.LIBRE;
    }
}
