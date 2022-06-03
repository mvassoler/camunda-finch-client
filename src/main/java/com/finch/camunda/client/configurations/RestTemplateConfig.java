package com.finch.camunda.client.configurations;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value(value = "${jobs.sincronismo.tempo-conexao}")
    private String connectTimeout;

    @Value(value = "${jobs.sincronismo.tempo-espera}")
    private String timeOut;

    @Bean
    public RestTemplate restTemplate (RestTemplateBuilder builder){
        return  builder
                .setConnectTimeout(Duration.ofMillis(Integer.valueOf(connectTimeout)))
                .setReadTimeout(Duration.ofMillis(Integer.valueOf(timeOut)))
                .build();
    }

}
