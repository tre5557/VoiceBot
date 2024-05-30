package org.voicebot.service.openai.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Data
public class OpenAIClient {
    private final String token;
    private final RestTemplate restTemplate;


    public ChatCompletionResponse createChatCompletion(
            ChatCompletionRequest request
    ) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        httpHeaders.set("Content-type", "application/json");

        HttpEntity<ChatCompletionRequest> httpEntity = new HttpEntity<>(request, httpHeaders);

        ResponseEntity<ChatCompletionResponse> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, httpEntity, ChatCompletionResponse.class
        );
        return responseEntity.getBody();
    }

    //    public ChatCompletionObject createChatCompletion(
//            String message
//    ) {
//        String url = "https://api.openai.com/v1/chat/completions";
//
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("Authorization", "Bearer " + token);
//        httpHeaders.set("Content-type", "application/json");
//
//        String request = """
//                {
//                    "model": "gpt-3.5-turbo-0125",
//                    "messages": [
//                      {
//                        "role": "system",
//                        "content": "You are a Spanish AI person.Be friendly, ask how you're doing and what's new with the other person. You were created to communicate in Spanish and teach Spanish to your interlocutors. Answer only in Spanish at all times.Always end sentences with a question so that the other person keeps talking. Please provide answers that do not exceed 800 characters"
//                      },
//                      {
//                        "role": "user",
//                        "content": "%s"
//                      }
//                    ]
//                  }
//                """.formatted(message);
//
//        HttpEntity<String> httpEntity = new HttpEntity<>(request, httpHeaders);
//
//        ResponseEntity<ChatCompletionObject> responseEntity = restTemplate.exchange(
//                url, HttpMethod.POST, httpEntity, ChatCompletionObject.class
//        );
//        return responseEntity.getBody();
//    }
    public byte[] getSpeechFromText(String text) throws IOException, InterruptedException {
        // Реализация запроса к OpenAI API для получения озвучки текста


        String url = "https://api.openai.com/v1/audio/speech";

        Map<String, String> data = new HashMap<>();
        data.put("model", "tts-1");
        data.put("input", text);
        data.put("voice", "alloy");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(data);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        return response.body();
    }

}
