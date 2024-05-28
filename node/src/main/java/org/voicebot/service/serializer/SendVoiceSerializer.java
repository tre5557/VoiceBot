package org.voicebot.service.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

public class SendVoiceSerializer {

    public static String serializeSendVoice(SendVoice sendVoice) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = mapper.createObjectNode();

        // Преобразование других полей объекта SendVoice в JSON
        jsonNode.put("chatId", sendVoice.getChatId());
        // Добавление поля caption
        if (sendVoice.getCaption() != null) {
            jsonNode.put("caption", sendVoice.getCaption().toString());
        }

        // Преобразование файла в Base64
        InputFile inputFile = (InputFile) sendVoice.getVoice();
        InputStream inputStream = null;

        // Проверяем тип объекта, хранящегося в InputFile, и получаем InputStream
//        if (inputFile.getNewMediaFile() instanceof File) {
//            inputStream = new FileInputStream((File) inputFile.getMedia());
//        } else if (inputFile.getNewMediaFile() instanceof FileInputStream) {
//            inputStream = (InputStream) inputFile.getMedia();
//        } else if (inputFile.getMedia() instanceof byte[]) {
//            inputStream = new ByteArrayInputStream((byte[]) inputFile.getMedia());
//        }
        inputStream = (InputStream) inputFile.getNewMediaStream();

        if (inputStream != null) {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                IOUtils.copy(inputStream, baos);
                byte[] fileContent = baos.toByteArray();
                String encodedFile = Base64.getEncoder().encodeToString(fileContent);
                jsonNode.put("voice", encodedFile);
            }
        }

        return mapper.writeValueAsString(jsonNode);
    }
}