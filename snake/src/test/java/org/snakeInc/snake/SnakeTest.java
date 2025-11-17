package org.snakeInc.snake;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.model.Direction;
import org.snakeinc.snake.model.Game;

public class SnakeTest {

    Game game = new Game();

    @Test
    public void snakeEatApplesAfterMove_ReturnsCorrectBodySize() throws OutOfPlayException, SelfCollisionException {
        game.getBasket().addApple(game.getGrid().getTile(5, 4));
        game.getSnake().move(Direction.UP);
        Assertions.assertEquals(2, game.getSnake().getSize());
    }

    @Test
    void snakeMovesUp_ReturnCorrectHead() throws OutOfPlayException, SelfCollisionException {
        game.getSnake().move(Direction.UP);
        Assertions.assertEquals(5, game.getSnake().getHead().getX());
        Assertions.assertEquals(4, game.getSnake().getHead().getY());
    }

    @Test
    void snakeMovesOutOfBounds_ThrowsOutOfPlayException() {
        Assertions.assertThrows(OutOfPlayException.class, () -> {
            for (int i = 0; i < GameParams.TILES_Y;  i++) {
                game.getSnake().move(Direction.UP);
            }
        });
    }

    @Test
    void snakeEatsItself_ThrowsSelfCollisionException() throws OutOfPlayException, SelfCollisionException {
        game.getBasket().addApple(game.getGrid().getTile(5, 4));
        game.getBasket().addApple(game.getGrid().getTile(5, 3));
        game.getBasket().addApple(game.getGrid().getTile(5, 2));
        for (int i = 0; i < 4; i++) {
            game.iterate(Direction.UP);
        }
        game.iterate(Direction.LEFT);
        game.iterate(Direction.DOWN);
        Assertions.assertThrows(SelfCollisionException.class, () -> {
            game.iterate(Direction.RIGHT);
        });
    }

}
