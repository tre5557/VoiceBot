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
        var history = chatGptHistoryService.addMessageToHistory(
                userId,
                Message.builder()
                        .content(userTextInput)
                        .role("user")
                        .build()
        );

        Message systemMessage = Message.builder()
                .role("system")
                .content("You are a AI teacher of spanish language. You must talk only in English and Spanish. Be friendly, ask how you're doing and what's new with the other person. " +
                        "Always end sentences with a question so that the other person keeps talking. " +
                        "Please provide answers that do not exceed 800 characters")
                .build();

        List<Message> messagesWithSystem = new ArrayList<>();
        messagesWithSystem.add(systemMessage);
        messagesWithSystem.addAll(history.chatMessages());

        var request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo-0125")
                .messages(messagesWithSystem)
                .build();
        var response = openAIClient.createChatCompletion(request);

        var messageFromGpt = response.choices().get(0)
                .message();

        chatGptHistoryService.addMessageToHistory(userId, messageFromGpt);

        return messageFromGpt.content();
    }


}
