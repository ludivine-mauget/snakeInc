package org.snakeinc.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.snakeinc.api.model.Category;
import org.snakeinc.api.model.PlayerDto;
import org.snakeinc.api.model.PlayerParams;

import java.time.LocalDate;


@ExtendWith(MockitoExtension.class)
class PlayerServiceTests {
    PlayerService playerService = new PlayerService();

    @Test
    void testCreatePlayer_ShouldReturnPlayer() {
        // Arrange
        PlayerParams params = new PlayerParams("Alice", 20);
        PlayerDto expectedPlayer = new PlayerDto(1, "Alice", 20, Category.JUNIOR, LocalDate.now());
        // Act
        PlayerDto createdPlayer = playerService.createPlayer(params);
        // Assert
        assert createdPlayer.getId().equals(expectedPlayer.getId());
        assert createdPlayer.getName().equals(expectedPlayer.getName());
        assert createdPlayer.getAge() == expectedPlayer.getAge();
        assert createdPlayer.getCategory() == expectedPlayer.getCategory();
    }
}
