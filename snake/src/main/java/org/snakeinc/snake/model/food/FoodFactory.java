package org.snakeinc.snake.model.food;

import org.snakeinc.snake.model.Cell;

public class FoodFactory {

    public static Food createFoodInCell(FoodType foodType, Cell cell) {
        return switch (foodType) {
            case APPLE -> createAppleInCell(cell);
            case BROCCOLI -> createBroccoliInCell(cell);
        };
    }

    private static Apple createAppleInCell(Cell cell) {
        Apple apple = new Apple();
        cell.addFood(apple);
        apple.setCurrentCell(cell);
        return apple;
    }

    private static Broccoli createBroccoliInCell(Cell cell) {
        Broccoli broccoli = new Broccoli();
        cell.addFood(broccoli);
        broccoli.setCurrentCell(cell);
        return broccoli;
    }

}
