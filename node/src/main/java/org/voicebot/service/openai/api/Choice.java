package org.voicebot.service.openai.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

public record Choice (
        @JsonProperty("message") Message message
) {}
