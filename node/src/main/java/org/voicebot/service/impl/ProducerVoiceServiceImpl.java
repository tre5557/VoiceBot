package org.voicebot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.voicebot.service.ProducerVoiceService;
import org.voicebot.service.serializer.SendVoiceSerializer;

import static org.voicebot.model.RabbitQueue.ANSWER_VOICE_MESSAGE;

@Service
public class ProducerVoiceServiceImpl implements ProducerVoiceService {

        private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public ProducerVoiceServiceImpl(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {

            this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

        @Override
        public void producerAnswer(SendVoice sendVoice) {

            try {
                // Сериализация объекта SendVoice
                String serializedSendVoice = SendVoiceSerializer.serializeSendVoice(sendVoice);
                // Отправка сериализованного объекта в очередь
                rabbitTemplate.convertAndSend(ANSWER_VOICE_MESSAGE, serializedSendVoice);
            } catch (Exception e) {
                e.printStackTrace();
                // Логирование ошибки или выполнение других действий
            }
        }

}
