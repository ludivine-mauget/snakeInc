package org.snakeinc.snake.model;

import java.util.ArrayList;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;

public class Snake {

    private final ArrayList<Cell> body;
    private final AppleEatenListener onAppleEatenListener;
    private final Grid grid;

    public Snake(AppleEatenListener listener, Grid grid) {
        this.body = new ArrayList<>();
        this.onAppleEatenListener = listener;
        this.grid = grid;
        Cell head = grid.getTile(GameParams.SNAKE_DEFAULT_X, GameParams.SNAKE_DEFAULT_Y);
        head.addSnake(this);
        body.add(head);
    }

    public int getSize() {
        return body.size();
    }

    public Cell getHead() {
        return body.getFirst();
    }

    public void eat(Apple apple, Cell cell) {
        body.addFirst(cell);
        cell.addSnake(this);
        onAppleEatenListener.onAppleEaten(apple, cell);
    }

    public void move(Direction direction) throws OutOfPlayException, SelfCollisionException {
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

        // Eat apple :
        if (newHead.containsAnApple()) {
            this.eat(newHead.getApple(), newHead);
            return;
        }

        // The snake did not eat :
        newHead.addSnake(this);
        body.addFirst(newHead);

        body.getLast().removeSnake();
        body.removeLast();

    }

}
