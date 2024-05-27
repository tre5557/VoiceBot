package org.voicebot.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.voicebot.dao.AppUserDAO;
import org.voicebot.dao.RawDataDAO;
import org.voicebot.entity.AppUser;
import org.voicebot.entity.RawData;
import org.voicebot.service.MainService;
import org.voicebot.service.ProducerService;
import org.voicebot.service.openai.api.ChatCompletionRequest;
import org.voicebot.service.openai.api.OpenAIClient;

import static org.voicebot.entity.enums.UserState.BASIC_STATE;
import static org.voicebot.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;
import static org.voicebot.service.enums.ServiceCommands.*;

//главный сервис через который будет просиходить обработка сообщений
@Service
@Log4j
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    private final ProducerService producerService;
    private final AppUserDAO appUserDAO;

    private final OpenAIClient openAIClient;

    public MainServiceImpl(RawDataDAO rawDataDAO, ProducerService producerService, AppUserDAO appUserDAO, OpenAIClient openAIClient) {
        this.rawDataDAO = rawDataDAO;
        this.producerService = producerService;
        this.appUserDAO = appUserDAO;
        this.openAIClient = openAIClient;
    }

    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var userState = appUser.getState();
        var text = update.getMessage().getText();
        var output = "";


        // тест блока с AI

        var chatCompletionResponse = openAIClient.createChatCompletion(text);

        var messageFromGpt = chatCompletionResponse.choices().get(0).message().content();
        var chatId = update.getMessage().getChatId();
        sendAnswer(messageFromGpt, chatId);



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

    @Override
    public void processDocMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if(IsNotAllowedToSendContent(chatId,appUser)){
            return;
        }

        //TODO добавить сохранение документа
        var answer = "Документ успешно загружен! Ссылка для скачивания: http://test/get-doc/777";
        sendAnswer(answer,chatId);


    }


    @Override
    public void processPhotoMessage(Update update) {
        saveRawData(update);
        var appUser = findOrSaveAppUser(update);
        var chatId = update.getMessage().getChatId();
        if(IsNotAllowedToSendContent(chatId,appUser)){
            return;
        }

        //TODO добавить сохранение документа
        var answer = "Фото успешно загружено! Ссылка для скачивания: http://test/get-photo/777";
        sendAnswer(answer,chatId);
        sendAnswer(answer,chatId);

    }

    private boolean IsNotAllowedToSendContent(Long chatId, AppUser appUser) {
        var userState = appUser.getState();
        if (!appUser.getIsActive()){
            var error = "Зарегестрируйте свою учетную запись!";
            sendAnswer(error,chatId);
            return true;
        }
        else if(!BASIC_STATE.equals(userState)) {
            var error = "отмените текущую команду с помощью /cancel для отправки файлов";
            sendAnswer(error,chatId);
            return true;
        }
        return false;
    }

    private void sendAnswer(String output, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(output);
        producerService.producerAnswer(sendMessage);
    }

    private String processServiceCommand(AppUser appUser, String cmd) {
        if (REGISTRATION.isEqual(cmd)){
            //TODO добавить реализацию позже
            return "Временно недоступна";
        } else if (HELP.isEqual(cmd)){
            return help();
        } else if (START.isEqual(cmd)) {
            return "Приветствую, чтобы ввести список доступных комманд, введите /help";
        }
        else {
            System.out.println("test");
           return "Неизвестная команда, чтобы ввести список доступных комманд, введите /help";
        }
    }

    private String help() {
        return "Список доступных команд \n"
                + "/cancel - отмена выполнения текущей команды \n"
                + "/registration - регистрация пользователя";
    }

    private String cancelProcess(AppUser appUser) {
        appUser.setState(BASIC_STATE);
        appUserDAO.save(appUser);
        return "Команда отменена";
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
