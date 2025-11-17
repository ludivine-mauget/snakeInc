package org.snakeinc.snake.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.Data;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.model.food.*;

@Data
public class Basket {

    private Grid grid;
    private List<Apple> apples;
    private List<Broccoli> broccolis;

    public Basket(Grid grid) {
        apples = new ArrayList<>();
        broccolis = new ArrayList<>();
        this.grid = grid;
    }

    public void addFood(Cell cell, FoodType foodType) {
        if (cell == null) {
            var random = new Random();
            while (cell == null || cell.containsASnake() || cell.containsFood()) {
                cell = grid.getTile(random.nextInt(0, GameParams.TILES_X), random.nextInt(0, GameParams.TILES_Y));
            }
        }
        Food food = FoodFactory.createFoodInCell(foodType, cell);
        switch (food) {
            case Apple apple -> apples.add(apple);
            case Broccoli broccoli -> broccolis.add(broccoli);
            default -> {}
        }
    }

    public void removeFoodInCell(Food food, Cell cell) {
        cell.removeFood();
        switch (food) {
            case Apple apple -> apples.remove(apple);
            case Broccoli broccoli -> broccolis.remove(broccoli);
            default -> {}
        }
    }

    public boolean isEmpty() {
        return apples.isEmpty() && broccolis.isEmpty();
    }

    private void refill(int number, FoodType foodType) {
        switch (foodType) {
            case APPLE -> {
                for (int i = 0; i < number; i++) {
                    addFood(null, FoodType.APPLE);
                }
            }
            case BROCCOLI -> {
                for (int i = 0; i < number; i++) {
                    addFood(null, FoodType.BROCCOLI);
                }
            }
            default -> {}
        }
    }

    public void refillIfNeeded(int nApples, int nBroccolis) {
        int missingApple = nApples - apples.size();
        int missingBroccoli = nBroccolis - broccolis.size();
        if (missingApple > 0) {
            refill(missingApple, FoodType.APPLE);
        }
        if (missingBroccoli > 0) {
            refill(missingBroccoli, FoodType.BROCCOLI);
        }
    }

}
