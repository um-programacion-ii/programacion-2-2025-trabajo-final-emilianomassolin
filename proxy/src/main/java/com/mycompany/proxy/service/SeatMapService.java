package com.mycompany.proxy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.proxy.service.dto.SeatDTO;
import com.mycompany.proxy.service.dto.SeatMapDTO;
import com.mycompany.proxy.service.dto.SeatStatus;
import com.mycompany.proxy.service.dto.redis.RedisSeatData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SeatMapService {

    private static final Logger log = LoggerFactory.getLogger(SeatMapService.class);

    private final StringRedisTemplate redisTemplate;
    private final String seatKeyPrefix;
    private final ObjectMapper objectMapper;

    public SeatMapService(
        StringRedisTemplate redisTemplate,
        @Value("${proxy.redis.seat-key-prefix:evento_}") String seatKeyPrefix,
        ObjectMapper objectMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.seatKeyPrefix = seatKeyPrefix;
        this.objectMapper = objectMapper;
    }

    /**
     * Construye el mapa completo de asientos:
     * - filas/columnas vienen del backend.
     * - Redis sólo tiene asientos Bloqueado/Vendido.
     * - Lo que no está en Redis se considera LIBRE.
     */
    public SeatMapDTO getSeatMapForEvent(Long eventoId, int filas, int columnas) {
        Map<String, SeatStatus> ocupados = loadSeatsFromRedis(eventoId);

        List<SeatDTO> asientos = new ArrayList<>(filas * columnas);
        for (int f = 1; f <= filas; f++) {
            for (int c = 1; c <= columnas; c++) {
                String key = f + ":" + c;
                SeatStatus status = ocupados.getOrDefault(key, SeatStatus.LIBRE);
                asientos.add(new SeatDTO(f, c, status));
            }
        }

        return new SeatMapDTO(eventoId, filas, columnas, asientos);
    }

    /**
     * Lee la key evento_<ID> de Redis y devuelve un map "fila:columna" -> SeatStatus.
     */
    private Map<String, SeatStatus> loadSeatsFromRedis(Long eventoId) {
        Map<String, SeatStatus> result = new HashMap<>();

        String key = seatKeyPrefix + eventoId; // ejemplo: evento_1
        String json = redisTemplate.opsForValue().get(key);

        if (json == null) {
            log.debug("No hay datos en Redis para la key {}", key);
            return result;
        }

        try {
            RedisSeatData data = objectMapper.readValue(json, RedisSeatData.class);
            if (data.getAsientos() == null) {
                return result;
            }

            data.getAsientos().forEach(seat -> {
                String compositeKey = seat.getFila() + ":" + seat.getColumna();
                String estado = seat.getEstado();
                SeatStatus status = SeatStatus.LIBRE;

                if ("Bloqueado".equalsIgnoreCase(estado)) {
                    status = SeatStatus.BLOQUEADO;
                } else if ("Vendido".equalsIgnoreCase(estado)) {
                    status = SeatStatus.OCUPADO;
                }

                result.put(compositeKey, status);
            });

        } catch (Exception e) {
            log.error("Error parseando JSON de Redis para evento {}: {}", eventoId, e.getMessage(), e);
        }

        return result;
    }
}
