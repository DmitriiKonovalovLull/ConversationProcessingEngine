package com.aisales.api.dto;

import jakarta.validation.constraints.NotBlank;

public record IncomingMessageRequest(
        @NotBlank String channel,
        @NotBlank String userId,
        @NotBlank String text
) {}
