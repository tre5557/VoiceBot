package org.voicebot.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voicebot.service.ConsumerService;
import org.voicebot.service.MainService;

import static org.voicebot.model.RabbitQueue.*;
// Считываем сообщения из брокера
@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {
    private final MainService mainService;

    public ConsumerServiceImpl(MainService mainService) {
        this.mainService = mainService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)// слушает очередь и принимает оттуда update
    public void consumeTextMessageUpdates(Update update) {
        log.debug("NODE : text message is received");
        mainService.processTextMessage(update);// передаем update в mainservice
    }
}
