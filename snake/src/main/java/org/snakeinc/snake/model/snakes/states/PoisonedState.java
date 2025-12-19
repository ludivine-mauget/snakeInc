package org.snakeinc.snake.model.snakes.states;

import org.snakeinc.snake.exception.MalnutritionException;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.model.Direction;
import org.snakeinc.snake.model.food.Apple;
import org.snakeinc.snake.model.food.Broccoli;
import org.snakeinc.snake.model.food.Food;
import org.snakeinc.snake.model.snakes.Snake;

import java.sql.DatabaseMetaData;

public class PoisonedState extends State {
    public PoisonedState(Snake snake) {
        super(snake);
    }

    @Override
    public void onEat(Food food) {
        switch (food){
            case Apple apple -> {
                if (apple.isPoisoned()){
                    snake.setState(new DamagedState(snake));
                }
            }
            case Broccoli broccoli -> {
                snake.setState(new HealthyState(snake));
            }
            default -> snake.setState(this);
        }
    }

    @Override
    public void onMove(Direction direction) throws OutOfPlayException, SelfCollisionException, MalnutritionException {
            snake.move(transformDirection(direction));
        }

    @Override
    public Direction transformDirection(Direction direction) {
        if (direction == Direction.UP) {
            return Direction.DOWN;
        } else if (direction == Direction.DOWN) {
            return Direction.UP;
        }
        return direction;
    }

}
