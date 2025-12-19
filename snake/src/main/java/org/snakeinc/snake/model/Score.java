package org.snakeinc.snake.model;

import lombok.Getter;
import lombok.Setter;
import org.snakeinc.snake.model.food.Apple;
import org.snakeinc.snake.model.food.Broccoli;
import org.snakeinc.snake.model.food.Food;
import org.snakeinc.snake.model.food.FoodEatenListener;

@Getter
@Setter
public class Score {
    private int points = 0;
    private int foodsEaten = 0;
    private int movesMade = 0;

    public void incrementPoints(int delta) {
        this.points += delta;
    }

    public void incrementFoodsEaten() {
        this.foodsEaten += 1;
    }

    public void incrementMovesMade() {
        this.movesMade += 1;
    }

}
