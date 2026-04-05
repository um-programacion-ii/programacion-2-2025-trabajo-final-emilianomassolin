package com.mycompany.myapp.config;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CatedraClientConfig {

    @Bean
    @Primary
    public RestTemplate catedraRestTemplate(CatedraProperties properties, RestTemplateBuilder builder) {

        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
            request.getHeaders().setBearerAuth(properties.getToken().trim());
            return execution.execute(request, body);
        };

        return builder
            .rootUri(properties.getBaseUrl())
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(10))
            .additionalInterceptors(authInterceptor)
            .build();
    }
}
