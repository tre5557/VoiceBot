package org.voicebot.service;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


public interface AnswerConsumer {
    void consume(SendMessage sendmessage);

}
