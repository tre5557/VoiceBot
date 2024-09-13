package org.voicebot.service.openai.api;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatGptService {
    private final OpenAIClient openAIClient;
    private final ChatGptHistoryService chatGptHistoryService;

    @Nonnull
    public String getResponseChatForUser(
            Long userId,
            String userTextInput
    ) {
        chatGptHistoryService.createHistoryIfNotExist(userId);

        // Добавляем сообщение пользователя в историю и получаем обновленный объект истории
        var chatHistoryEntity = chatGptHistoryService.addMessageToHistory(
                userId,
                Message.builder()
                        .content(userTextInput)
                        .role("user")
                        .build()
        );

        // Создаем системное сообщение
        Message systemMessage = Message.builder()
                .role("system")
                .content("You are an AI teacher of English language. You must talk only in English and Russian. Be friendly, ask how you're doing and what's new with the other person. " +
                        "Always end sentences with a question so that the other person keeps talking. " +
                        "Please provide answers that do not exceed 800 characters")
                .build();

        // Создаем список сообщений для запроса
        List<Message> messagesWithSystem = new ArrayList<>();
        messagesWithSystem.add(systemMessage);
        messagesWithSystem.addAll(chatHistoryEntity.getChatHistory().chatMessages());

        // Создаем запрос к OpenAI
        var request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo-0125")
                .messages(messagesWithSystem)
                .build();

        // Получаем ответ от OpenAI
        var response = openAIClient.createChatCompletion(request);

        // Добавляем ответ GPT в историю
        var messageFromGpt = response.choices().get(0)
                .message();

        chatGptHistoryService.addMessageToHistory(userId, messageFromGpt);

        return messageFromGpt.content();
    }
}
