package org.snakeinc.snake.model;

import lombok.Getter;
import org.snakeinc.snake.exception.MalnutritionException;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.model.food.Apple;
import org.snakeinc.snake.model.food.Broccoli;
import org.snakeinc.snake.model.food.Food;
import org.snakeinc.snake.model.food.FoodEatenListener;
import org.snakeinc.snake.model.snakes.Anaconda;
import org.snakeinc.snake.model.snakes.BoaConstrictor;
import org.snakeinc.snake.model.snakes.Python;
import org.snakeinc.snake.model.snakes.Snake;
import org.snakeinc.snake.model.strategy.DifficultPlacementStrategy;
import org.snakeinc.snake.model.strategy.EasyPlacementStrategy;
import org.snakeinc.snake.model.strategy.FoodPlacementStrategy;
import org.snakeinc.snake.model.strategy.RandomPlacementStrategy;

@Getter
public class Game {

    private final Grid grid;
    private final Basket basket;
    private final Snake snake;
    private final Score score;
    private final FoodPlacementStrategy placementStrategy;

    public Game() {
        grid = new Grid();
        placementStrategy = randomPlacementStrategy();
        basket = new Basket(grid, placementStrategy);

        FoodEatenListener listener = (food, cell) -> {
            basket.removeFoodInCell(food, cell);
            onFoodEaten(food, cell);
        };

        snake = RandomSnake(listener, grid);
        basket.setSnake(snake);

        basket.refillIfNeeded(1, 1);

        score = new Score();
    }

    public void iterate(Direction direction) throws OutOfPlayException, SelfCollisionException, MalnutritionException {
        Direction transformedDirection = snake.getState().transformDirection(direction);
        Direction lastEffective = snake.getLastEffectiveDirection();

        if (transformedDirection == lastEffective.opposite()) {
            direction = findInputDirectionFor(lastEffective);
        }

        snake.getState().onMove(direction);
        score.incrementMovesMade();
        basket.refillIfNeeded(1, 1);
    }

    private Direction findInputDirectionFor(Direction desiredEffective) {
        for (Direction input : Direction.values()) {
            if (snake.getState().transformDirection(input) == desiredEffective) {
                return input;
            }
        }
        return desiredEffective;
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

    private FoodPlacementStrategy randomPlacementStrategy() {
        int pick = (int) (Math.random() * 3);
        return switch (pick) {
            case 0 -> new RandomPlacementStrategy();
            case 1 -> new EasyPlacementStrategy();
            case 2 -> new DifficultPlacementStrategy();
            default -> throw new IllegalStateException("Unexpected value: " + pick);
        };
    }

    private void onFoodEaten(Food food, Cell cell) {
        switch (food) {
            case Apple apple -> {
                if (apple.isPoisoned()) {
                    score.incrementPoints(0);
                } else {
                    score.incrementPoints(2);
                }
                score.incrementFoodsEaten();
            }
            case Broccoli broccoli -> {
                if (broccoli.isSteamed()) {
                    score.incrementPoints(0);
                } else {
                    score.incrementPoints(1);
                }
                score.incrementFoodsEaten();
            }
            default -> throw new IllegalStateException("Unexpected value: " + food);
        }
    }

    public String getSnakeType() {
        return snake.getClass().getSimpleName().toLowerCase();
    }

}
