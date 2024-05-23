package org.voicebot.service.telegramVoiceService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Data
public class OpenAITrans {

    private final String token;
    private final RestTemplate restTemplate;


    @SneakyThrows
    public TranscriptionResponse createTranscription(
            CreateTranscriptionRequest request
    ) {
        String url = "https://api.openai.com/v1/audio/transcriptions";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        httpHeaders.set("Content-type", "multipart/form-data");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(request.audioFile()));
        body.add("model", request.model());

        var httpEntity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<TranscriptionResponse> responseEntity = restTemplate.exchange(
                url, HttpMethod.POST, httpEntity, TranscriptionResponse.class
        );
        return responseEntity.getBody();
    }

    public byte[] getSpeechFromText(String text) throws IOException, InterruptedException {
        // Реализация запроса к OpenAI API для получения озвучки текста


        String url = "https://api.openai.com/v1/audio/speech";

        Map<String, String> data = new HashMap<>();
        data.put("model", "tts-1");
        data.put("input", text);
        data.put("voice", "alloy");  // Укажите нужный голос и язык

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