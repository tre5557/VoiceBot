package org.voicebot.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.voicebot.controller.UpdateController;
import org.voicebot.service.AnswerVoiceConsumer;

import static org.voicebot.model.RabbitQueue.ANSWER_MESSAGE;

public class AnswerVoiceConsumerImpl implements AnswerVoiceConsumer {
    private final UpdateController updateController;

    public AnswerVoiceConsumerImpl(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)// слушает очередь answer
    public void consume(SendVoice sendVoiceMessage) {
        updateController.setVoiceVIew(sendVoiceMessage);
    }
}
