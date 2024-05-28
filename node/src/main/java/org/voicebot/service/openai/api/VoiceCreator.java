package org.voicebot.service.openai.api;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;


import java.io.ByteArrayInputStream;
import java.io.IOException;
@Service

public class VoiceCreator {
    private final OpenAIClient openAIClient;

    public VoiceCreator(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    public InputFile createVoice(String text) {

        byte[] audioBytes = new byte[0];
        try {
            audioBytes = openAIClient.getSpeechFromText(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        InputFile audioFile = new InputFile(new ByteArrayInputStream(audioBytes), "voice.ogg");
        return audioFile;

    }
}
