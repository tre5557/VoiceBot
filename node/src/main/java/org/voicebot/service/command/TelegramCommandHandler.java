package org.voicebot.service.command;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TelegramCommandHandler {
    String processCommand(Message update);

    TelegramCommands getSupportedCommand();
}
