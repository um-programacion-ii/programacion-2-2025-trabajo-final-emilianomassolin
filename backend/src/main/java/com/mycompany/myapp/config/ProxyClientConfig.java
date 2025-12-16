package com.mycompany.myapp.config;

import java.time.Duration;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ProxyClientConfig {

    @Bean
    public RestTemplate proxyRestTemplate(ProxyProperties properties, RestTemplateBuilder builder) {

        ClientHttpRequestInterceptor authInterceptor = (request, body, execution) -> {
            request.getHeaders().setBearerAuth(properties.getToken().trim());
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
