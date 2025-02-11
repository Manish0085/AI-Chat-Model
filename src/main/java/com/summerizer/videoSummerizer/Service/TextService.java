package com.summerizer.videoSummerizer.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TextService {

    private final WebClient webClient;

    public TextService(@Value("${huggingface.api.url}") String apiUrl,
                                @Value("${huggingface.api.token}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public Mono<String> summarizeText(String text) {
        return webClient.post()
                .bodyValue("{\"inputs\":\"" + text + "\"}")
                .retrieve()
                .bodyToMono(String.class);
    }
}
