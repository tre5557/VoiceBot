package org.voicebot.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.voicebot.service.UpdateProducer;
import org.voicebot.service.telegramVoiceService.TelergamVoiceHandler;
import org.voicebot.service.telegramVoiceService.VoiceCreator.TelegramVoiceCreator;
import org.voicebot.utils.MessageUtils;

import static org.voicebot.model.RabbitQueue.*;

@Component
@Log4j
public class UpdateController {

    private TelegramBot telegramBot;
    private final MessageUtils messageUtils;
    private final UpdateProducer updateProducer;
    private final TelergamVoiceHandler telergamVoiceHandler;
    private final TelegramVoiceCreator telergamVoicecreator;


    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer, TelergamVoiceHandler telergamVoiceHandler, TelegramVoiceCreator telergamVoicecreator) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
        this.telergamVoiceHandler = telergamVoiceHandler;
        this.telergamVoicecreator = telergamVoicecreator;
    }

    public void registerBot(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update){
        if (update == null){
            log.error("Recieved update is null");
            return;
        }

        if (update.hasMessage()){
            distributeMessageByType(update);
        }
        else{
            log.error("Unsupported message type is received!" + update);
        }
    }

    private void distributeMessageByType(Update update){
        var message = update.getMessage();
           if (message.hasText()){
//           textToVoiceHandler(update);
            processTextMessage(update);
        }
        else if (message.hasVoice()){
            processVoiceMessage(update);
        }
        else if (message.hasDocument()){
            processDocMessage(update);
        }
        else if (message.hasPhoto()){
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

    public void setVIew(SendMessage sendMessage) {
        telegramBot.sendAnswearMessage(sendMessage);
    }
    public void setVoiceVIew(SendVoice sendVoice) {
        telegramBot.sendAnswearVoice(sendVoice);
    }

    private void processPhotoMessage(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }



    private void processDocMessage(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }
// Метод передает наш update в указанную очередь rabbitMQ
    private void processTextMessage(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }
    private void processVoiceMessage(Update update) {
        var transcription = telergamVoiceHandler.processVoice(update);
        var message = update.getMessage();
        message.setText(transcription);
        update.setMessage(message);
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }
//    private void textToVoiceHandler(Update update) {
//        String text = update.getMessage().getText();
////        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
//        InputFile audiofile =  telergamVoicecreator.createVoice(text);
//        SendVoice sendVoice = messageUtils.generateSendMessageWithAudio(update, audiofile);
//        setVoiceVIew(sendVoice);
//    }
}
