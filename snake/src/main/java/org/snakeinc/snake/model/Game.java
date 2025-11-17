package org.snakeinc.snake.model;

import lombok.Getter;
import org.snakeinc.snake.exception.MalnutritionException;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.model.food.FoodEatenListener;
import org.snakeinc.snake.model.snakes.Anaconda;
import org.snakeinc.snake.model.snakes.BoaConstrictor;
import org.snakeinc.snake.model.snakes.Python;
import org.snakeinc.snake.model.snakes.Snake;

@Getter
public class Game {

    private final Grid grid;
    private final Basket basket;
    private final Snake snake;

    public Game() {
        grid = new Grid();
        basket = new Basket(grid);
        basket.refillIfNeeded(1, 1);
        snake = RandomSnake(basket::removeFoodInCell, grid);
    }

    public void iterate(Direction direction) throws OutOfPlayException, SelfCollisionException, MalnutritionException {
        snake.move(direction);
        basket.refillIfNeeded(1, 1);
    }

    private Snake RandomSnake(FoodEatenListener listener, Grid grid) {
        int pick = (int) (Math.random() * 3);
        return switch (pick) {
            case 0 -> new Anaconda(listener, grid);
            case 1 -> new BoaConstrictor(listener, grid);
            case 2 -> new Python(listener, grid);
            default -> throw new IllegalStateException("Unexpected value: " + pick);
        };
    }


}
