package org.snakeinc.snake.model.strategy;

import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.snakes.Snake;

public interface FoodPlacementStrategy {
    Cell findPlacementCell(Grid grid, Snake snake);
    String getStrategyName();
}

