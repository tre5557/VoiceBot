package org.voicebot.service.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.voicebot.service.ProducerService;

import static org.voicebot.model.RabbitQueue.ANSWER_MESSAGE;
// нужен для отправки ответов с Ноды в Брокер
@Service
public class ProducerServiceImpl implements ProducerService {
    private final RabbitTemplate rabbitTemplate;

    public ProducerServiceImpl(RabbitTemplate rabbitTemplate) {

        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void producerAnswer(SendMessage sendMessage) {
            rabbitTemplate.convertAndSend(ANSWER_MESSAGE, sendMessage);
        }

}
