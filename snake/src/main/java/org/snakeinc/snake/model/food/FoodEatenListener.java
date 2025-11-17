package org.snakeinc.snake.model.food;

import org.snakeinc.snake.model.Cell;

public abstract interface FoodEatenListener {
    void  onFoodEaten(Food food, Cell cell);
}
