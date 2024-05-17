package org.voicebot.service.openai.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


import java.util.List;

public record ChatCompletionObject(
        @JsonProperty("choices") List<Choice> choices
){}
