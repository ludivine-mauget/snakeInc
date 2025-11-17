package org.snakeinc.snake.model;

import lombok.Getter;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;

@Getter
public class Game {

    private final Grid grid;
    private final Basket basket;
    private final Snake snake;

    public Game() {
        grid = new Grid();
        basket = new Basket(grid);
        basket.refillIfNeeded(1);
        snake = new Snake(basket::removeAppleInCell, grid);
    }

    public void iterate(Direction direction) throws OutOfPlayException, SelfCollisionException {
        snake.move(direction);
        basket.refillIfNeeded(1);
    }


}
