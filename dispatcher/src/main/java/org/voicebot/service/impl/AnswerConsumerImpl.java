package org.voicebot.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.voicebot.controller.UpdateController;
import org.voicebot.service.AnswerConsumer;

import static org.voicebot.model.RabbitQueue.*;

@Service
public class AnswerConsumerImpl implements AnswerConsumer {
    private final UpdateController updateController;

    public AnswerConsumerImpl(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendmessage) {

    updateController.setVIew(sendmessage);
    }
}
