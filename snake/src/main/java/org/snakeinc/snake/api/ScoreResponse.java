package org.snakeinc.snake.api;

import java.time.LocalDateTime;

public record ScoreResponse(Integer id, String snake, int score, LocalDateTime playedAt, Integer playerId) {
}

