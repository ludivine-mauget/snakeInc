package org.snakeinc.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.snakeinc.api.entity.Player;
import org.snakeinc.api.model.Category;
import org.snakeinc.api.model.PlayerDto;
import org.snakeinc.api.model.PlayerParams;
import org.snakeinc.api.repository.PlayerRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PlayerServiceTests {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void testCreatePlayer_ShouldReturnPlayer() {
        // Arrange
        PlayerParams params = new PlayerParams("Alice", 20);
        Player savedPlayer = new Player(1, "Alice", 20, Category.JUNIOR, LocalDate.now(), null);

        when(playerRepository.save(any())).thenReturn(savedPlayer);

        // Act
        PlayerDto createdPlayer = playerService.createPlayer(params);

        // Assert
        assert createdPlayer.getId().equals(1);
        assert createdPlayer.getName().equals("Alice");
        assert createdPlayer.getAge() == 20;
        assert createdPlayer.getCategory() == Category.JUNIOR;
    }

    @Test
    void testGetPlayerById_ShouldReturnPlayer() {
        // Arrange
        Player player = new Player(1, "Alice", 20, Category.JUNIOR, LocalDate.now(), null);
        when(playerRepository.findById(1)).thenReturn(Optional.of(player));

        // Act
        PlayerDto result = playerService.getPlayerById(1);

        // Assert
        assert result != null;
        assert result.getId().equals(1);
        assert result.getName().equals("Alice");
    }

    @Test
    void testGetPlayerById_NotFound_ShouldReturnNull() {
        // Arrange
        when(playerRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        PlayerDto result = playerService.getPlayerById(999);

        // Assert
        assert result == null;
    }
}
