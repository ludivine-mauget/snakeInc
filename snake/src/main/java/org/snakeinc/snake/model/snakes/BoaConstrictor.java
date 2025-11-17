package org.snakeinc.snake.model.snakes;

import org.snakeinc.snake.model.food.Broccoli;
import org.snakeinc.snake.model.food.Food;
import org.snakeinc.snake.model.food.FoodEatenListener;
import org.snakeinc.snake.model.Grid;
import org.snakeinc.snake.model.food.Apple;
import org.snakeinc.snake.model.Cell;

public non-sealed class BoaConstrictor extends Snake {
    public BoaConstrictor(FoodEatenListener listener, Grid grid) {
        super(listener, grid);
    }

    @Override
    public void eat(Food food, Cell cell){
        switch (food) {
            case Apple apple -> {
                var cellOfTail = body.removeLast();
                cellOfTail.removeSnake();
                onFoodEatenListener.onFoodEaten(apple, cell);
            }
            case Broccoli broccoli -> {
                onFoodEatenListener.onFoodEaten(broccoli, cell);
            }
            default -> throw new IllegalArgumentException("Unsupported food type: " + food.getClass().getSimpleName());
        }
    }
}
