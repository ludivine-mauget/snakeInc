package org.snakeInc.snake;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.exception.MalnutritionException;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.model.Direction;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.food.*;
import org.snakeinc.snake.model.snakes.Anaconda;
import org.snakeinc.snake.model.snakes.BoaConstrictor;
import org.snakeinc.snake.model.snakes.Python;
import org.snakeinc.snake.model.snakes.Snake;

public class SnakeTest {

    Grid grid;
    FoodEatenListener listener;

    @BeforeEach
    void setUp() {
        Food.setMovementEnabled(false);
        grid = new Grid();
        listener = (food, cell) -> {};
    }

    @ParameterizedTest
    @CsvSource({
        "ANACONDA, 4",
        "PYTHON, 3",
        "BOA, 2"
    })
    public void snakeEatApplesAfterMove_ReturnsCorrectBodySize(String snakeType, int expectedSize) throws OutOfPlayException, SelfCollisionException, MalnutritionException {
        Snake snake = createSnake(snakeType);
        grid.getTile(4, 4).addFood(new Apple());
        snake.move(Direction.UP);
        Assertions.assertEquals(expectedSize, snake.getSize());
    }

    @ParameterizedTest
    @CsvSource({
        "ANACONDA",
        "PYTHON",
        "BOA"
    })
    void snakeMovesUp_ReturnCorrectHead(String snakeType) throws OutOfPlayException, SelfCollisionException, MalnutritionException {
        Snake snake = createSnake(snakeType);
        snake.move(Direction.UP);
        Assertions.assertEquals(4, snake.getHead().getX());
        Assertions.assertEquals(4, snake.getHead().getY());
    }

    @ParameterizedTest
    @CsvSource({
            "ANACONDA",
            "PYTHON",
            "BOA"
    })
    void anacondaMovesOutOfBounds_ThrowsOutOfPlayException(String snakeType) {
        Snake snake = createSnake(snakeType);
        Assertions.assertThrows(OutOfPlayException.class, () -> {
            for (int i = 0; i < GameParams.TILES_Y; i++) {
                snake.move(Direction.UP);
            }
        });
    }

    @ParameterizedTest
    @CsvSource({
        "ANACONDA"
    })
    void anacondaEatsItself_ThrowsSelfCollisionException(String snakeType) throws OutOfPlayException, SelfCollisionException, MalnutritionException {
        Snake snake = createSnake(snakeType);
        grid.getTile(4, 4).addFood(new Apple());
        grid.getTile(4, 3).addFood(new Apple());
        grid.getTile(4, 2).addFood(new Apple());
        for (int i = 0; i < 3; i++) {
            snake.move(Direction.UP);
        }
        snake.move(Direction.LEFT);
        snake.move(Direction.DOWN);
        Assertions.assertThrows(SelfCollisionException.class, () -> {
            snake.move(Direction.RIGHT);
        });
    }

    private Snake createSnake(String snakeType) {
        return switch (snakeType) {
            case "ANACONDA" -> new Anaconda(listener, grid);
            case "PYTHON" -> new Python(listener, grid);
            case "BOA" -> new BoaConstrictor(listener, grid);
            default -> throw new IllegalArgumentException("Unknown snake type: " + snakeType);
        };
    }

}
