package org.snakeinc.snake.model.observer;

public interface SnakePublisher {

    void attach(SnakeSubscriber subscriber);

    void detach(SnakeSubscriber subscriber);

    void notifySubscribers();
}

