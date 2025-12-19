package org.snakeinc.snake.model.strategy;

import java.util.Random;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.snakes.Snake;

public class RandomPlacementStrategy implements FoodPlacementStrategy {

    private final Random random = new Random();

    @Override
    public Cell findPlacementCell(Grid grid, Snake snake) {
        Cell cell = null;
        while (cell == null || cell.containsASnake() || cell.containsFood()) {
            int x = random.nextInt(GameParams.TILES_X);
            int y = random.nextInt(GameParams.TILES_Y);
            cell = grid.getTile(x, y);
        }
        return cell;
    }

    @Override
    public String getStrategyName() {
        return "Random";
    }
}

