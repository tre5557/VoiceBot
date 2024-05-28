package org.voicebot.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;

public interface ProducerVoiceService {
    void producerAnswer(SendVoice sendVoice);
}
