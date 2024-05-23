package org.voicebot.service.telegramVoiceService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@AllArgsConstructor
public class TranscribeVoiceToTextService {

    private final OpenAITrans openAITrans;

    public String transcribe(File audioFile) {
        var response = openAITrans.createTranscription(CreateTranscriptionRequest.builder()
                .audioFile(audioFile)
                .model("whisper-1")
                .build());
        return response.text();
    }

}
