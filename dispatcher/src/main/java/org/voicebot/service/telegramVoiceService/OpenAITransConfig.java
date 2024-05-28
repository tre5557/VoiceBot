package org.voicebot.service.telegramVoiceService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAITransConfig {

    @Bean
    public OpenAITrans openAITrans(
            @Value("${openai.token}") String token,
            RestTemplateBuilder restTemplateBuilder
    ) {
        return new OpenAITrans(token, restTemplateBuilder.build());
    }

}
