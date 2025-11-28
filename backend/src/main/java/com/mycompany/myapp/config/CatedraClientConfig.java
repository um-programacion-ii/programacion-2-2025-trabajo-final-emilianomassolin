package com.mycompany.myapp.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;

@Configuration
public class CatedraClientConfig {

    @Bean
    public RestTemplate catedraRestTemplate(CatedraProperties properties, RestTemplateBuilder builder) {

        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + properties.getToken());
            return execution.execute(request, body);
        };

        return builder
            .rootUri(properties.getBaseUrl())
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(10))
            .additionalInterceptors(List.of(authInterceptor))
            .build();
    }
}
