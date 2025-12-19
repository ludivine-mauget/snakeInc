package org.snakeinc.snake.model.snakes;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.snakeinc.snake.GameParams;
import org.snakeinc.snake.exception.MalnutritionException;
import org.snakeinc.snake.exception.OutOfPlayException;
import org.snakeinc.snake.exception.SelfCollisionException;
import org.snakeinc.snake.model.*;
import org.snakeinc.snake.model.food.Food;
import org.snakeinc.snake.model.food.FoodEatenListener;
import org.snakeinc.snake.model.observer.SnakeSubscriber;
import org.snakeinc.snake.model.observer.SnakePublisher;
import org.snakeinc.snake.model.snakes.states.HealthyState;
import org.snakeinc.snake.model.snakes.states.State;

public abstract sealed class Snake implements SnakePublisher permits Anaconda, BoaConstrictor, Python {

    protected final ArrayList<Cell> body;
    protected final FoodEatenListener onFoodEatenListener;
    private final Grid grid;
    @Getter
    @Setter
    private State state;
    @Getter
    private Direction lastEffectiveDirection = Direction.RIGHT;
    private final List<SnakeSubscriber> subscribers = new ArrayList<>();

    public Snake(FoodEatenListener listener, Grid grid) {
        this.body = new ArrayList<>();
        this.onFoodEatenListener = listener;
        this.grid = grid;
        this.state = new HealthyState(this);
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

    @Override
    public void attach(SnakeSubscriber subscriber) {
        if (!subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
        }
    }

    @Override
    public void detach(SnakeSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        Cell head = getHead();
        if (head != null) {
            for (SnakeSubscriber observer : subscribers) {
                observer.onSnakeHeadMoved(head);
            }
        }
    }

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

                lastEffectiveDirection = direction;

                if (newHead.containsFood()) {
                    Food food = newHead.getFood();
                    this.eat(food, newHead);
                    state.onEat(food);
                    return;
                }

                newHead.addSnake(this);
                body.addFirst(newHead);

                body.getLast().removeSnake();
                body.removeLast();

                notifySubscribers();
    }
}
