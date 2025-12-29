package org.snakeinc.snake.api;

import java.time.LocalDate;

public record PlayerResponse(Integer id, String name, int age, String category, LocalDate createdAt) {
}

