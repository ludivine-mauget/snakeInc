package org.snakeinc.api.exception;

public class InvalidSnakeTypeException extends RuntimeException {
    public InvalidSnakeTypeException(String snakeType) {
        super("Invalid snake type: " + snakeType + ". Valid values are: python, anaconda, boaConstrictor");
    }
}

