package org.snakeinc.api.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(Integer playerId) {
        super("Player with ID " + playerId + " not found");
    }
}

