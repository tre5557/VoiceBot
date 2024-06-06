package org.voicebot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {
    // под каждую очередь будет свой метод
    void consumeTextMessageUpdates(Update update);
}
