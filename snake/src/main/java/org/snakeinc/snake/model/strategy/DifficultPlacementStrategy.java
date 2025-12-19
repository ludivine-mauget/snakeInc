package org.snakeinc.snake.model.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.snakes.Snake;

public class DifficultPlacementStrategy implements FoodPlacementStrategy {

    private static final int BORDER_DISTANCE = 2;
    private static final int MIN_DISTANCE_FROM_HEAD = 8;
    private final Random random = new Random();

    @Override
    public Cell findPlacementCell(Grid grid, Snake snake) {
        Cell head = snake.getHead();

        List<Cell> borderCells = new ArrayList<>();
        List<Cell> difficultCells = new ArrayList<>();

        for (int x = 0; x < GameParams.TILES_X; x++) {
            for (int y = 0; y < GameParams.TILES_Y; y++) {
                Cell cell = grid.getTile(x, y);
                if (cell == null || cell.containsASnake() || cell.containsFood()) {
                    continue;
                }

                boolean isNearBorder = isNearBorder(x, y);
                boolean isFarFromHead = head == null || getDistance(x, y, head.getX(), head.getY()) >= MIN_DISTANCE_FROM_HEAD;

                if (isNearBorder && isFarFromHead) {
                    difficultCells.add(cell);
                }
                else if (isNearBorder) {
                    borderCells.add(cell);
                }
            }
        }

        if (!difficultCells.isEmpty()) {
            return difficultCells.get(random.nextInt(difficultCells.size()));
        }

        if (!borderCells.isEmpty()) {
            return borderCells.get(random.nextInt(borderCells.size()));
        }

        return findFarthestCellFromHead(grid, snake);
    }

    private boolean isNearBorder(int x, int y) {
        return x < BORDER_DISTANCE
            || x >= GameParams.TILES_X - BORDER_DISTANCE
            || y < BORDER_DISTANCE
            || y >= GameParams.TILES_Y - BORDER_DISTANCE;
    }

    private int getDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    private Cell findFarthestCellFromHead(Grid grid, Snake snake) {
        Cell head = snake.getHead();
        Cell farthest = null;
        int maxDistance = -1;

        for (int x = 0; x < GameParams.TILES_X; x++) {
            for (int y = 0; y < GameParams.TILES_Y; y++) {
                Cell cell = grid.getTile(x, y);
                if (cell == null || cell.containsASnake() || cell.containsFood()) {
                    continue;
                }

                int distance = (head != null) ? getDistance(x, y, head.getX(), head.getY()) : 0;
                if (distance > maxDistance) {
                    maxDistance = distance;
                    farthest = cell;
                }
            }
        }

        if (farthest == null) {
            return new RandomPlacementStrategy().findPlacementCell(grid, snake);
        }

        return farthest;
    }

    @Override
    public String getStrategyName() {
        return "Difficult";
    }
}
