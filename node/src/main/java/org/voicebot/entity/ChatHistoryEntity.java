package org.voicebot.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.voicebot.service.openai.api.ChatHistory;
import org.voicebot.service.openai.api.Message;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_history")
public class ChatHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "text", nullable = false)
    private String chatMessages; // Сериализованное представление List<Message>

    @CreationTimestamp
    private LocalDateTime timestamp;

    // Метод для десериализации ChatHistory из строки
    public ChatHistory getChatHistory() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Message> messages = mapper.readValue(chatMessages,
                    mapper.getTypeFactory().constructCollectionType(List.class, Message.class));
            return new ChatHistory(messages);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize chat history", e);
        }
    }

    // Метод для сериализации ChatHistory в строку
    public void setChatHistory(ChatHistory chatHistory) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            this.chatMessages = mapper.writeValueAsString(chatHistory.chatMessages());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize chat history", e);
        }
    }
}
