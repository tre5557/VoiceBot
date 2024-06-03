package org.voicebot.service.command.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.voicebot.service.ProducerService;
import org.voicebot.service.command.TelegramCommandHandler;
import org.voicebot.service.command.TelegramCommands;


@Component
public class StartCommandHandler implements TelegramCommandHandler {

    private final String HELLO_MESSAGE = "Hello! This is a virtual AI interlocutor " +
            "with whom you can communicate by voice. " +
            "You can use different languages! Try it out!";

    @Override
    public String processCommand(Message message) {
        String answer = HELLO_MESSAGE;
        return answer;
    }
    @Override
    public TelegramCommands getSupportedCommand() {
        return TelegramCommands.START;
    }
}
