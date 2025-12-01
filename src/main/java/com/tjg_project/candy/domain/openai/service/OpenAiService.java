package com.tjg_project.candy.domain.openai.service;

import com.tjg_project.candy.domain.openai.dto.OpenAiRequest;
import com.tjg_project.candy.domain.openai.dto.OpenAiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OpenAiService {

//    @Value("${openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String ask(String question) {

        String url = "https://api.openai.com/v1/chat/completions";

        OpenAiRequest request = new OpenAiRequest(
                "gpt-4o-mini",
                Collections.singletonList(new OpenAiRequest.Message("user", question))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<OpenAiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<OpenAiResponse> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, OpenAiResponse.class);

        return response.getBody().getChoices().get(0).getMessage().getContent();
    }
}