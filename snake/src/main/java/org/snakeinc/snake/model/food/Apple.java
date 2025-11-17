package org.snakeinc.snake.model.food;

import lombok.Getter;

@Getter
public non-sealed class Apple extends Food {
    private final boolean isPoisonous;
    public Apple() {
        var random = Math.random();
        this.isPoisonous = random < 0.1;
    }

    public boolean isPoisonous() {
        return isPoisonous;
    }
}
