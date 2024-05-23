package org.voicebot.service.openai.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


import java.util.List;
//record - класс, предназначенный для хранения данных, он неизменяем и легок. Отлично подходит для этого случая
public record ChatCompletionObject(
        @JsonProperty("choices") List<Choice> choices
){}
