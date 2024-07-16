package org.voicebot.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.voicebot.controller.UpdateController;
import org.voicebot.service.AnswerVoiceConsumer;
import org.voicebot.service.deserializer.SendVoiceDeserializer;


import java.util.ArrayList;
import java.util.List;

import static org.voicebot.model.RabbitQueue.ANSWER_VOICE_MESSAGE;

@Service
public class AnswerVoiceConsumerImpl implements AnswerVoiceConsumer {
    private final UpdateController updateController;

    public AnswerVoiceConsumerImpl(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    @RabbitListener(queues = ANSWER_VOICE_MESSAGE)// слушает очередь answer_voice
    public void consume(String message) {
        try {
            // Десериализация сообщения
            SendVoice sendVoice = SendVoiceDeserializer.deserializeSendVoice(message);
            // Обработка объекта SendVoice
            System.out.println("Received <" + sendVoice + ">");

            updateController.setVoiceVIew(sendVoice);
            // Здесь можно добавить дополнительную логику для обработки полученного сообщения
        } catch (Exception e) {
            e.printStackTrace();
            // Логирование ошибки или выполнение других действий
        }
    }
}
