package org.voicebot.service.telegramVoiceService.VoiceCreator;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.voicebot.service.telegramVoiceService.OpenAITrans;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
@Service
public class TelegramVoiceCreator {

    private final OpenAITrans openAITrans;

    public TelegramVoiceCreator(OpenAITrans openAITrans) {
        this.openAITrans = openAITrans;
    }

    public InputFile createVoice(String text) {

        byte[] audioBytes = new byte[0];
        try {
            audioBytes = openAITrans.getSpeechFromText(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        InputFile audioFile = new InputFile(new ByteArrayInputStream(audioBytes), "voice.ogg");
            return audioFile;

    }
}
