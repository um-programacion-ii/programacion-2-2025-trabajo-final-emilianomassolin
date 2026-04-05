package com.mycompany.proxy.config;

import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    // MISMO base64-secret del backend JHipster
    private static final String JWT_SECRET_BASE64 =
        "OTU3YTE2Yjc1NTAxN2JiOTBiOGFhMjVkYTBiNzliNTMyMDI2ZmJkOTMxYzAwOGVhNTRhMGU1MWExMGJjMWZhZTAyOGJjMWZmODgwYzlhZGMyMGU0N2UwZGZiYjZhY2ZjNjJjNjQ4YWQ5MTkwODhiMWViOWFmZDU4MGJmZmRiMjU=";

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] secretBytes = Base64.getDecoder().decode(JWT_SECRET_BASE64);
        SecretKey key = new SecretKeySpec(secretBytes, "HmacSHA512");

        // 👇 CLAVE: el token del backend es HS512
        return NimbusJwtDecoder
            .withSecretKey(key)
            .macAlgorithm(MacAlgorithm.HS512)
            .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/proxy/**").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt());

        return http.build();
    }
}
