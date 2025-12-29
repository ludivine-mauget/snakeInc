package org.snakeinc.snake.api;

import java.time.LocalDateTime;

public record ScoreRequest(String snake, Integer score, LocalDateTime playedAt, Integer playerId) {
}

