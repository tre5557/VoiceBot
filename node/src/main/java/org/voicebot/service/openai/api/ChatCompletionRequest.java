package org.voicebot.service.openai.api;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatCompletionRequest(
        String model,
        String message
) {
}