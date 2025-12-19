package org.snakeinc.snake.model.observer;

import org.snakeinc.snake.model.Cell;

public interface SnakeSubscriber {
    void onSnakeHeadMoved(Cell headPosition);
}

