package com.mycompany.myapp.service.mobile;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MobileSessionService {

    private static final Logger log = LoggerFactory.getLogger(MobileSessionService.class);

    // TTL de sesión en minutos (ej: 30)
    private static final long SESSION_TTL_MINUTES = 30L;

    private final RedisTemplate<String, MobileSessionState> redisTemplate;

    public MobileSessionService(RedisTemplate<String, MobileSessionState> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String redisKey(String username) {
        return "mobile:session:" + username;
    }

    public Optional<MobileSessionState> getSession(String username) {
        String key = redisKey(username);
        MobileSessionState state = redisTemplate.opsForValue().get(key);

        if (state == null) {
            return Optional.empty();
        }

        // si querés validar por expiresAt dentro del objeto, podés hacerlo aquí
        if (state.getExpiresAt() != null && state.getExpiresAt().isBefore(Instant.now())) {
            log.info("Sesión expirada para usuario {}, eliminando de Redis", username);
            redisTemplate.delete(key);
            return Optional.empty();
        }

        return Optional.of(state);
    }

    public MobileSessionState getOrCreateSession(String username) {
        return getSession(username).orElseGet(() -> {
            MobileSessionState nueva = MobileSessionState.nuevaSesion(username);
            saveSession(nueva);
            return nueva;
        });
    }

    public void saveSession(MobileSessionState state) {
        String key = redisKey(state.getUsername());

        Instant now = Instant.now();
        state.setLastUpdatedAt(now);
        state.setExpiresAt(now.plus(Duration.ofMinutes(SESSION_TTL_MINUTES)));

        redisTemplate
            .opsForValue()
            .set(key, state, Duration.ofMinutes(SESSION_TTL_MINUTES));

        log.debug("Sesión mobile guardada para usuario {} etapa={} eventoId={}",
            state.getUsername(), state.getEtapa(), state.getEventoId());
    }

    public void clearSession(String username) {
        String key = redisKey(username);
        redisTemplate.delete(key);
        log.info("Sesión mobile eliminada para usuario {}", username);
    }

    // helpers específicos para el flujo

    public MobileSessionState actualizarSeleccionEvento(
        String username,
        Long eventoId
    ) {
        MobileSessionState state = getOrCreateSession(username);
        state.setEventoId(eventoId);
        state.getAsientos().clear(); // reset selección
        state.setEtapa(MobileFlowStage.ASIENTOS);
        saveSession(state);
        return state;
    }

    public MobileSessionState actualizarAsientos(
        String username,
        java.util.List<MobileSeatSelection> asientos
    ) {
        MobileSessionState state = getOrCreateSession(username);
        state.setAsientos(asientos);
        // seguimos en ASIENTOS; el cambio de etapa lo harás cuando bloquees y pases a DATOS_PERSONAS
        saveSession(state);
        return state;
    }

    public MobileSessionState cambiarEtapa(
        String username,
        MobileFlowStage nuevaEtapa
    ) {
        MobileSessionState state = getOrCreateSession(username);
        state.setEtapa(nuevaEtapa);
        saveSession(state);
        return state;
    }
}
