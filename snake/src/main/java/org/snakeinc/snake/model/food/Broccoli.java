package org.snakeinc.snake.model.food;

import lombok.Getter;
import lombok.Setter;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;

@Getter
public non-sealed class Broccoli extends Food {
    private final boolean isSteamed;
    @Setter
    private Grid grid;

    public Broccoli() {
        var random = Math.random();
        this.isSteamed = random < 0.5;
    }

    @Override
    protected Grid getGrid() {
        return grid;
    }
}

