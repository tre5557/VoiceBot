package org.voicebot.service.deserializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
import java.util.Base64;
@Component
public class SendVoiceDeserializer {
    public static SendVoice deserializeSendVoice(String message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(message);

        // Восстановление других полей объекта SendVoice
        String chatId = jsonNode.get("chatId").asText();
        String caption = null;
        if (jsonNode.has("caption")) {
            caption = jsonNode.get("caption").asText();
        }

        // Восстановите другие необходимые поля

        // Декодирование файла из Base64
        String encodedFile = jsonNode.get("voice").asText();
        byte[] fileContent = Base64.getDecoder().decode(encodedFile);
        InputFile inputFile = new InputFile(new ByteArrayInputStream(fileContent), "voice.ogg");

        SendVoice sendVoice = new SendVoice(chatId, inputFile);
        sendVoice.setCaption(caption);
        // Установите другие необходимые поля

        return sendVoice;
    }
}
