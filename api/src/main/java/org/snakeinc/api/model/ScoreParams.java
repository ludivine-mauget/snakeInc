package org.snakeinc.api.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScoreParams(
        @NotNull(message = "Snake type is required")
        String snake,

        @NotNull(message = "Score is required")
        @Min(value = 0, message = "Score cannot be negative")
        Integer score,

        @NotNull(message = "Played at is required")
        LocalDateTime playedAt,

        @NotNull(message = "Player ID is required")
        Integer playerId
) {
}

