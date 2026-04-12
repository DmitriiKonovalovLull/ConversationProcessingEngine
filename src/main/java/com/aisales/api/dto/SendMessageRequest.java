package com.aisales.api.dto;

import jakarta.validation.constraints.NotBlank;

public record SendMessageRequest(

        @NotBlank
        String channel,

        @NotBlank
        String userId,

        @NotBlank
        String text
) {}
