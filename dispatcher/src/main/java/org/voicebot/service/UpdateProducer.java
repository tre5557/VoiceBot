package org.voicebot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateProducer {
    void produce (String rabbitmq, Update update);
}
