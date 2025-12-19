package org.snakeinc.snake.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.snakeinc.snake.model.food.*;
import org.snakeinc.snake.model.snakes.Snake;
import org.snakeinc.snake.model.strategy.FoodPlacementStrategy;

@Getter
public class Basket {

    @Setter
    private Grid grid;
    private final List<Apple> apples;
    private final List<Broccoli> broccolis;
    private final FoodPlacementStrategy placementStrategy;
    @Setter
    private Snake snake;

    public Basket(Grid grid, FoodPlacementStrategy placementStrategy) {
        apples = new ArrayList<>();
        broccolis = new ArrayList<>();
        this.grid = grid;
        this.placementStrategy = placementStrategy;
    }

    public void addFood(Cell cell, FoodType foodType) {
        if (cell == null) {
            cell = placementStrategy.findPlacementCell(grid, snake);
        }
        Food food = FoodFactory.createFoodInCell(foodType, cell);

        switch (food) {
            case Apple apple -> {
                apple.setGrid(grid);
                apples.add(apple);
            }
            case Broccoli broccoli -> {
                broccoli.setGrid(grid);
                broccolis.add(broccoli);
            }
            default -> {}
        }

        if (snake != null) {
            snake.attach(food);
        }
    }

    public void removeFoodInCell(Food food, Cell cell) {
        cell.removeFood();

        if (snake != null) {
            snake.detach(food);
        }

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
