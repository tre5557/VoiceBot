package org.voicebot.controller;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;


@Component /* Спринг сможет создать из класса бин и поместить его в контекст*/
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}") /*Аннтотация от СПринг, которая достает значения из properties*/
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    private UpdateController updateController;

    public TelegramBot(UpdateController updateController){
        this.updateController = updateController;
    }

    @PostConstruct // аннотация, позволяет выполнять метод сразу после конструктора
    public void init(){
        updateController.registerBot(this);
    }

    @Override
    public String getBotUsername() {
        return "best_talking_bot";
    }

    @Override
    public String getBotToken() {
        return "7042409055:AAFHfRwLJblyeHcasggLRVB0HFlfFlGH1RU";
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    public void sendAnswearMessage(SendMessage message){
        if (message != null){
            try{
                execute(message);
            } catch(TelegramApiException e ) {
               log.error(e);
            }
        }

    }


}
