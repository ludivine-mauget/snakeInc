package org.snakeinc.snake.model.snakes;

import org.snakeinc.snake.exception.MalnutritionException;
import org.snakeinc.snake.model.food.Apple;
import org.snakeinc.snake.model.food.Broccoli;
import org.snakeinc.snake.model.food.Food;
import org.snakeinc.snake.model.food.FoodEatenListener;
import org.snakeinc.snake.model.Cell;
import org.snakeinc.snake.model.Grid;

public non-sealed class Anaconda extends Snake {

    public Anaconda(FoodEatenListener listener, Grid grid) {
        super(listener, grid);
    }

    @Override
    public void eat(Food food, Cell cell) throws MalnutritionException {
        switch (food) {
            case Apple apple -> {
                body.addFirst(cell);
                cell.addSnake(this);
                onFoodEatenListener.onFoodEaten(apple, cell);
            }
            case Broccoli broccoli -> {
                if (body.size() <= 2) {
                    throw new MalnutritionException();
                }
                for (int i = 0; i < 2; i++) {
                    var cellOfTail = body.removeLast();
                    cellOfTail.removeSnake();
                }
                onFoodEatenListener.onFoodEaten(broccoli, cell);
            }

            default -> throw new IllegalArgumentException("Unsupported food type: " + food.getClass().getSimpleName());
        }


    }
}
