package com.mipt.sem2.hw4.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Value("${external.api.base-url}")
    private String externalApiBaseUrl;

    @Bean
    public RestClient externalTasksRestClient(RestClient.Builder builder) {
        ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        ((SimpleClientHttpRequestFactory) requestFactory).setConnectTimeout(Duration.ofSeconds(2));
        ((SimpleClientHttpRequestFactory) requestFactory).setReadTimeout(Duration.ofSeconds(3));

        return builder
            .baseUrl(externalApiBaseUrl)
            .defaultHeader(HttpHeaders.USER_AGENT, "ResilientGateway/1.0")
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .requestFactory(requestFactory)
            .build();
    }
}