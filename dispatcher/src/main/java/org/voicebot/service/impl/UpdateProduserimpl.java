package org.voicebot.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voicebot.service.UpdateProducer;

@Service
@Log4j
public class UpdateProduserimpl implements UpdateProducer {
    @Override
    public void produce(String rabbitmw, Update update) {
        log.debug(update.getMessage().getText());
    }
}
