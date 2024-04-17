package org.voicebot.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voicebot.service.UpdateProducer;
import org.voicebot.utils.MessageUtils;

import static org.voicebot.model.RabbitQueue.*;

@Component
@Log4j
public class UpdateController {

    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }

    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update){
        if (update == null){
            log.error("Recieved update is null");
            return;
        }

        if (update.getMessage() != null){
            distributeMessageByType(update);
        }
        else{
            log.error("Unsupported message type is received!" + update);
        }
    }

    private void distributeMessageByType(Update update){
        var message = update.getMessage();
        if (message.getText() != null){
            processTextMessage(update);
        }
        else if (message.getDocument() != null){
            processDocMessage(update);
        }
        else if (message.getPhoto() != null){
            processPhotoMessage(update);
        }
        else {
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setUnsupportedMessageTypeView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,"Неподдерживаемое сообщение");
        setVIew(sendMessage);
    }

    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils.generateSendMessageWithText(update,"Файл получен. Обрабатывается...");
        setVIew(sendMessage);
    }

    private void setVIew(SendMessage sendMessage) {
        telegramBot.sendAnswearMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }



    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

}
