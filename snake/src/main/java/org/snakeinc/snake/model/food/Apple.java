package org.snakeinc.snake.model.food;

import lombok.Getter;
import lombok.Setter;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;

@Getter
public non-sealed class Apple extends Food {
    private final boolean isPoisoned;
    @Setter
    private Grid grid;

    public Apple() {
        var random = Math.random();
        this.isPoisoned = random < 0.1;
    }

    @Override
    protected Grid getGrid() {
        return grid;
    }

}
