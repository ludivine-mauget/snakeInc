package org.snakeinc.snake.model.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.snakes.Snake;

public class EasyPlacementStrategy implements FoodPlacementStrategy {

    private static final int EASY_RADIUS = 5;
    private final Random random = new Random();

    @Override
    public Cell findPlacementCell(Grid grid, Snake snake) {
        Cell head = snake.getHead();
        if (head == null) {
            return new RandomPlacementStrategy().findPlacementCell(grid, snake);
        }

        int headX = head.getX();
        int headY = head.getY();

        List<Cell> validCells = new ArrayList<>();
        for (int dx = -EASY_RADIUS; dx <= EASY_RADIUS; dx++) {
            for (int dy = -EASY_RADIUS; dy <= EASY_RADIUS; dy++) {
                int x = headX + dx;
                int y = headY + dy;

                if (dx == 0 && dy == 0) {
                    continue;
                }

                if (x >= 0 && x < GameParams.TILES_X && y >= 0 && y < GameParams.TILES_Y) {
                    Cell cell = grid.getTile(x, y);
                    if (cell != null && !cell.containsASnake() && !cell.containsFood()) {
                        validCells.add(cell);
                    }
                }
            }
        }

        if (!validCells.isEmpty()) {
            return validCells.get(random.nextInt(validCells.size()));
        }

        return new RandomPlacementStrategy().findPlacementCell(grid, snake);
    }

    @Override
    public String getStrategyName() {
        return "Easy";
    }
}

