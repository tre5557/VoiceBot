package org.voicebot.service.command;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
@AllArgsConstructor
public class TelegramCommandsDispatcher {

    private final List<TelegramCommandHandler> telegramCommandHandlerList;


    public String processCommand(Update update) {
        Message message = update.getMessage();
        String text = update.getMessage().getText();
        if (!isCommand(text)) {
            throw new IllegalArgumentException("Not a command passed");
        }

        var suitedHandler = telegramCommandHandlerList.stream()
                .filter(it -> it.getSupportedCommand().getCommandValue().equals(text))
                .findAny();
        if (suitedHandler.isEmpty()) {
          return "There is no such command";
        }
        return suitedHandler.orElseThrow().processCommand(message);
    }
    public boolean isCommand(String message) {
        return message.startsWith("/");
    }

}
