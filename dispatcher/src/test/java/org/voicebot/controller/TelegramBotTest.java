package org.voicebot.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import static org.mockito.Mockito.*;

public class TelegramBotTest {

    @Mock
    private UpdateController updateController;

    @InjectMocks
    private TelegramBot telegramBot;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        telegramBot = new TelegramBot(updateController);
    }

    @Test
    void testSendAnswearMessage() throws TelegramApiException {
        SendMessage sendMessage = mock(SendMessage.class);
        telegramBot.sendAnswearMessage(sendMessage);
        verify(telegramBot, times(1)).execute(sendMessage);
    }
    @Test
    void testOnUpdateReceived() {
        Update update = mock(Update.class);
        telegramBot.onUpdateReceived(update);
        verify(updateController, times(1)).processUpdate(update);
    }
}
