package org.snakeinc.snake.model.food;

public non-sealed class Broccoli extends Food {
    private final boolean isSteamed;
    public Broccoli() {
        var random = Math.random();
        this.isSteamed = random < 0.5;
    }

    public boolean isSteamed() {
        return isSteamed;
    }
}
