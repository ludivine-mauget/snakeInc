package org.snakeinc.snake.model.food;

import lombok.Getter;

@Getter
public non-sealed class Broccoli extends Food {
    private final boolean isSteamed;
    public Broccoli() {
        var random = Math.random();
        this.isSteamed = random < 0.5;
    }

}
