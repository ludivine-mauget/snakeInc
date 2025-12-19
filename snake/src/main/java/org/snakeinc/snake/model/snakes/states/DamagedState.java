package org.snakeinc.snake.model.snakes.states;

import org.snakeinc.snake.exception.MalnutritionException;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.model.Direction;
import org.snakeinc.snake.model.food.Food;
import org.snakeinc.snake.model.snakes.Snake;

public class DamagedState extends State {
    public DamagedState(Snake snake) {
        super(snake);
    }

    @Override
    public void onEat(Food food) {
        return;
    }

    @Override
    public void onMove(Direction direction) throws OutOfPlayException, SelfCollisionException, MalnutritionException {
        snake.move(transformDirection(direction));
    }

    @Override
    public Direction transformDirection(Direction direction) {
        return switch(direction) {
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
        };
    }
}
