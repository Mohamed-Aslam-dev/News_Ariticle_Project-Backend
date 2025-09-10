package com.ilayangudi_news_posting.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfiguration {
	
	@Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

}
