package com.andres.literalura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient gutendexRestClient() {
        return RestClient.builder()
                .baseUrl("https://gutendex.com")
                .build();
    }
}
