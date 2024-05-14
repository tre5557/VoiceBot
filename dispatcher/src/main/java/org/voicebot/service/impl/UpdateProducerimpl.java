package org.voicebot.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voicebot.service.UpdateProducer;
// класс реализует интерфейс, чтобы передавать апдейты в rabbitMQ
@Service
@Log4j
public class UpdateProducerimpl implements UpdateProducer {
// внедрим бин который подтягивается изнутри зависимости стартера spring-boot-starter-amqp, спринг сам создаст этот бин
    public final RabbitTemplate rabbitTemplate;

    public UpdateProducerimpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // метод принимает название очереди и Update
    @Override
    public void produce(String rabbitQueue, Update update) {
        log.debug(update.getMessage().getText());
        // передаем в метод имя и объект update который преобразуется далее в json
        rabbitTemplate.convertAndSend(rabbitQueue, update);
    }
}
