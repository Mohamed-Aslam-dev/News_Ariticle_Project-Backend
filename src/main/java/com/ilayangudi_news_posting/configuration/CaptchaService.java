package com.ilayangudi_news_posting.configuration;

import java.time.Duration;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CaptchaService {
	
	private final WebClient.Builder webClientBuilder;
	
	@Value("${captcha.secret.key}")
	private String secretKey;

    public CaptchaService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public boolean validate(String captchaResponse) {

        Map<String, Object> response = webClientBuilder.build()
        	    .post()
        	    .uri("https://www.google.com/recaptcha/api/siteverify?secret={secret}&response={response}", secretKey, captchaResponse)
        	    .retrieve()
        	    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
        	    .block(Duration.ofSeconds(5));

        return response != null && Boolean.TRUE.equals(response.get("success"));
    }

}
