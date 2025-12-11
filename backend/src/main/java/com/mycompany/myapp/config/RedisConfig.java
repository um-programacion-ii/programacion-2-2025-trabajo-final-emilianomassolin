package com.mycompany.myapp.config;

import com.mycompany.myapp.service.mobile.MobileSessionState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, MobileSessionState> mobileSessionRedisTemplate(
        RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, MobileSessionState> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Claves como strings
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // Valores como JSON
        Jackson2JsonRedisSerializer<MobileSessionState> valueSerializer =
            new Jackson2JsonRedisSerializer<>(MobileSessionState.class);

        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
