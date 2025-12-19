package org.snakeinc.snake.model.snakes.states;

import org.snakeinc.snake.exception.MalnutritionException;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.model.Direction;
import org.snakeinc.snake.model.food.Food;
import org.snakeinc.snake.model.snakes.Snake;

public abstract class State {
    protected Snake snake;

    State(Snake snake) {
        this.snake = snake;
    }
    public abstract void onEat(Food food);

    public abstract void onMove(Direction direction) throws OutOfPlayException, SelfCollisionException, MalnutritionException;

    /**
     * Returns the direction that will actually be used after state transformation.
     * This is used to check if a direction change would cause a self-collision.
     */
    public Direction transformDirection(Direction direction) {
        return direction;
    }
}
