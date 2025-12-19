package com.mycompany.myapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class DebugPropsConfig {

    private static final Logger log = LoggerFactory.getLogger(DebugPropsConfig.class);

    @Bean
    CommandLineRunner debugProps(Environment env) {
        return args -> {
            log.warn("ACTIVE PROFILES: {}", String.join(",", env.getActiveProfiles()));
            log.warn("PROP app.catedra.base-url = {}", env.getProperty("app.catedra.base-url"));
            log.warn("PROP app.catedra.token    = {}", env.getProperty("app.catedra.token"));
        };
    }
}
