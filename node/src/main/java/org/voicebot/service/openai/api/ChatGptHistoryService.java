package org.voicebot.service.openai.api;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.voicebot.dao.ChatHistoryDAO;
import org.voicebot.entity.ChatHistoryEntity;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatGptHistoryService {

    private final ChatHistoryDAO chatHistoryDAO;

    public Optional<ChatHistory> getUserHistory(Long userId) {
        return chatHistoryDAO.findAllByUserId(userId).stream()
                .findFirst()
                .map(ChatHistoryEntity::getChatHistory);
    }

    public void createHistory(Long userId) {
        ChatHistoryEntity chatHistoryEntity = ChatHistoryEntity.builder()
                .userId(userId)
                .chatMessages("[]") // Инициализируем пустым массивом сообщений
                .build();
        chatHistoryDAO.save(chatHistoryEntity);
    }

    public void clearHistory(Long userId) {
        chatHistoryDAO.deleteAllByUserId(userId);
    }

    public ChatHistoryEntity addMessageToHistory(Long userId, Message message) {
        Optional<ChatHistoryEntity> chatHistoryEntityOpt = chatHistoryDAO.findAllByUserId(userId).stream().findFirst();
        ChatHistoryEntity chatHistoryEntity = chatHistoryEntityOpt.orElseGet(() -> {
            ChatHistoryEntity newEntity = ChatHistoryEntity.builder()
                    .userId(userId)
                    .chatMessages("[]")
                    .build();
            chatHistoryDAO.save(newEntity);
            return newEntity;
        });

        ChatHistory chatHistory = chatHistoryEntity.getChatHistory();
        chatHistory.chatMessages().add(message);
        chatHistoryEntity.setChatHistory(chatHistory);

        return chatHistoryDAO.save(chatHistoryEntity);
    }

    public void createHistoryIfNotExist(Long userId) {
        if (chatHistoryDAO.findAllByUserId(userId).isEmpty()) {
            createHistory(userId);
        }
    }
}
