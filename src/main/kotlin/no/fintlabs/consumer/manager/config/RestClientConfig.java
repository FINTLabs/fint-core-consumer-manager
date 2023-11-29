package no.fintlabs.consumer.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient1(){
        return RestClient.create("https://api.github.com");
    }
}
