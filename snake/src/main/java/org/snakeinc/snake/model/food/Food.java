package org.snakeinc.snake.model.food;

import lombok.Getter;
import lombok.Setter;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.observer.SnakeSubscriber;

@Getter
@Setter
public sealed abstract class Food implements SnakeSubscriber permits Apple, Broccoli {

    private Cell currentCell;
    @Setter
    private static boolean movementEnabled = true;
    protected static final int REACTION_DISTANCE = 3;
    protected static final double MOVE_PROBABILITY = 0.2;

    @Override
    public void onSnakeHeadMoved(Cell headPosition) {
        if (!movementEnabled || currentCell == null || headPosition == null) {
            return;
        }

        int distance = getDistance(currentCell, headPosition);

        if (distance <= REACTION_DISTANCE && Math.random() < MOVE_PROBABILITY) {
            moveAway(headPosition);
        }
    }

    protected int getDistance(Cell cell1, Cell cell2) {
        return Math.abs(cell1.getX() - cell2.getX()) + Math.abs(cell1.getY() - cell2.getY());
    }

    protected abstract Grid getGrid();

    protected void moveAway(Cell headPosition) {
        if (getGrid() == null || getCurrentCell() == null) {
            return;
        }

        Cell currentPos = getCurrentCell();
        Cell bestCell = null;
        int maxDistance = -1;

        // Try to find a cell that is farther from the snake head
        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                if (dx == 0 && dy == 0) continue;

                int newX = currentPos.getX() + dx;
                int newY = currentPos.getY() + dy;

                // Check if the new position is valid
                if (newX < 0 || newX >= GameParams.TILES_X || newY < 0 || newY >= GameParams.TILES_Y) {
                    continue;
                }

                Cell candidateCell = getGrid().getTile(newX, newY);
                if (candidateCell == null || candidateCell.containsASnake() || candidateCell.containsFood()) {
                    continue;
                }

                int distanceFromHead = getDistance(candidateCell, headPosition);
                if (distanceFromHead > maxDistance) {
                    maxDistance = distanceFromHead;
                    bestCell = candidateCell;
                }
            }
        }

        // Move to the best cell if found
        if (bestCell != null && maxDistance > getDistance(currentPos, headPosition)) {
            currentPos.removeFood();
            bestCell.addFood(this);
            setCurrentCell(bestCell);
        }
    }
}
