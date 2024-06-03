package org.voicebot.service.command.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.voicebot.service.command.TelegramCommandHandler;
import org.voicebot.service.command.TelegramCommands;
import org.voicebot.service.openai.api.ChatGptHistoryService;

@Component
@AllArgsConstructor
public class ClearChatHistoryCommandHandler implements TelegramCommandHandler {
        private final ChatGptHistoryService chatGptHistoryService;

        private final String CLEAR_HISTORY_MESSAGE = "Your history has been cleared";

        @Override
        public String processCommand(Message message) {
            chatGptHistoryService.clearHistory(message.getChatId());
            String answer = CLEAR_HISTORY_MESSAGE;
            return answer;
        }

        @Override
        public TelegramCommands getSupportedCommand() {
            return TelegramCommands.CLEAR;
        }
    }
