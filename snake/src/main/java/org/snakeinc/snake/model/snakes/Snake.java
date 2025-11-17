package org.snakeinc.snake.model.snakes;

import java.util.ArrayList;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.exception.MalnutritionException;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.model.*;
import org.snakeinc.snake.model.food.Food;
import org.snakeinc.snake.model.food.FoodEatenListener;

public abstract sealed class Snake permits Anaconda, BoaConstrictor, Python {

    protected final ArrayList<Cell> body;
    protected final FoodEatenListener onFoodEatenListener;
    private final Grid grid;

    public Snake(FoodEatenListener listener, Grid grid) {
        this.body = new ArrayList<>();
        this.onFoodEatenListener = listener;
        this.grid = grid;
        for (int i = 1; i < GameParams.SNAKE_INITIAL_SIZE + 1; i++) {
            Cell bodyPart = grid.getTile(GameParams.SNAKE_DEFAULT_X - i, GameParams.SNAKE_DEFAULT_Y);
            bodyPart.addSnake(this);
            body.add(bodyPart);
        }

    }

    public int getSize() {
        return body.size();
    }

    public Cell getHead() {
        if (body.isEmpty()) {
            return null;
        }
        return body.getFirst();
    }

    public abstract void eat(Food food, Cell cell) throws MalnutritionException;

    public void move(Direction direction) throws OutOfPlayException, SelfCollisionException, MalnutritionException {
        if (body.isEmpty()) {
            throw new MalnutritionException();
        }

        int x = getHead().getX();
        int y = getHead().getY();
        switch (direction) {
            case UP -> y--;
            case DOWN -> y++;
            case LEFT -> x--;
            case RIGHT -> x++;
        }
        Cell newHead = grid.getTile(x, y);
        if (newHead == null) {
            throw new OutOfPlayException();
        }
        if (newHead.containsASnake()) {
            throw new SelfCollisionException();
        }

        // Eat food :
        if (newHead.containsFood()) {
            this.eat(newHead.getFood(), newHead);
            return;
        }

        // The snake did not eat :
        newHead.addSnake(this);
        body.addFirst(newHead);

        body.getLast().removeSnake();
        body.removeLast();

    }

}
