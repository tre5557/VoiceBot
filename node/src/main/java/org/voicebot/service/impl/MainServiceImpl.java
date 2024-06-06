package org.voicebot.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voicebot.dao.AppUserDAO;
import org.voicebot.dao.RawDataDAO;
import org.voicebot.entity.AppUser;
import org.voicebot.entity.RawData;
import org.voicebot.service.MainService;
import org.voicebot.service.ProducerService;
import org.voicebot.service.ProducerVoiceService;
import org.voicebot.service.command.TelegramCommandsDispatcher;
import org.voicebot.service.openai.api.ChatGptService;
import org.voicebot.service.openai.api.OpenAIClient;
import org.voicebot.service.openai.api.VoiceCreator;

import static org.voicebot.entity.enums.UserState.BASIC_STATE;
import static org.voicebot.service.enums.ServiceCommands.*;

//главный сервис через который будет просиходить обработка сообщений
@Service
@Log4j
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;
    private final ProducerVoiceService producerVoiceService;
    private final AppUserDAO appUserDAO;


    private final OpenAIClient openAIClient;
    private final ChatGptService chatGptService;
    private final VoiceCreator voiceCreator;
    private final TelegramCommandsDispatcher telegramCommandsDispatcher;





    public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService, ProducerVoiceService producerVoiceService, AppUserDAO appUserDAO, OpenAIClient openAIClient, ChatGptService chatGptService, VoiceCreator voiceCreator, TelegramCommandsDispatcher telegramCommandsDispatcher) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.producerVoiceService = producerVoiceService;
        this.appUserDAO = appUserDAO;
        this.openAIClient = openAIClient;
        this.chatGptService = chatGptService;
        this.voiceCreator = voiceCreator;
        this.telegramCommandsDispatcher = telegramCommandsDispatcher;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        var output = "";

        if(telegramCommandsDispatcher.isCommand(text)){
            output = telegramCommandsDispatcher.processCommand(update);
            sendAnswer(output,chatId);
        }
        else{
            // тест блока с AI

            var gptGeneratedText = chatGptService.getResponseChatForUser(chatId, text);
//        var chatCompletionResponse = openAIClient.createChatCompletion(text);

//        var messageFromGpt = chatCompletionResponse.choices().get(0).message().content();

            InputFile audioFile = generateVoiceFromText(gptGeneratedText);
            sendVoiceAnswer(audioFile,update,gptGeneratedText);
//        sendAnswer(messageFromGpt, chatId);



//        if(CANCEL.isEqual(text)){
//            output = cancelProcess(appUser);
//        } else if (BASIC_STATE.equals(userState)){
//            output = processServiceCommand(appUser, text);
//        } else if (WAIT_FOR_EMAIL_STATE.equals(userState)) {
//            // TODO добваить обработку емейла
//        } else {
//            log.error("Unknown user state " + userState);
//            output = "Неизвестная ошибка! Нажимте /cancel и попробуйте снова!";
//        }
//
//     var chatId = update.getMessage().getChatId();
//        log.debug("NODE : answer is ready");
//        sendAnswer(output, chatId);
        }




    }

    private InputFile generateVoiceFromText(String messageFromGpt) {
        InputFile audiofile =  voiceCreator.createVoice(messageFromGpt);
        return audiofile;
    }







    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }
    private void sendVoiceAnswer(InputFile audio, Update update,String textFromGpt) {
        Long chatId = update.getMessage().getChatId();
        SendVoice sendVoice = new SendVoice();
        sendVoice.setChatId(chatId);
        sendVoice.setVoice(audio);
        if (textFromGpt != null){
            sendVoice.setCaption(textFromGpt);
        }

        producerVoiceService.producerAnswer(sendVoice);
    }

    //сохранение пользователя в базу
    private AppUser findOrSaveAppUser(Update update){
        User telegramUser = update.getMessage().getFrom();
        AppUser persistentAppUser = appUserDAO.findAppUserByTelegramUserId(telegramUser.getId());
        if (persistentAppUser == null){
            AppUser transientAppUser = AppUser.builder()
                    .telegramUserId(telegramUser.getId())
                    .username(telegramUser.getUserName())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    // TODO изменить значени по умолчанию после добавления регистрации
                    .isActive(true)
                    .state(BASIC_STATE)
                    .build();
            return appUserDAO.save(transientAppUser);
        }
        return persistentAppUser;
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
                rawDataDAO.save(rawData);//метод save спринг создал автоматически
    }
}
