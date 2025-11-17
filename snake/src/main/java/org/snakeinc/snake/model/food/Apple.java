package org.snakeinc.snake.model.food;

import lombok.Getter;

@Getter
public non-sealed class Apple extends Food {
    private final boolean isPoisoned;
    public Apple() {
        var random = Math.random();
        this.isPoisoned = random < 0.1;
    }

}
