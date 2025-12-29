package org.snakeinc.api.controller;

import org.junit.jupiter.api.Test;
import org.snakeinc.api.ApiApplication;
import org.snakeinc.api.model.PlayerDto;
import org.snakeinc.api.model.PlayerParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = ApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")

public class PlayerControllerTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void postPlayer_ShouldReturnPlayer() {
        // Arrange
        PlayerParams playerParams = new PlayerParams("Bob", 25);
        // Act
        ResponseEntity<PlayerDto> response = restTemplate.postForEntity("/api/v1/players", playerParams, PlayerDto.class);
        // Assert
        assert response.getStatusCode().is2xxSuccessful();
        PlayerDto createdPlayer = response.getBody();
        assert createdPlayer != null;
        assert createdPlayer.getName().equals("Bob");
        assert createdPlayer.getAge() == 25;
    }

    @Test
    public void postPlayer_InvalidName_ShouldReturnBadRequest() {
        // Arrange
        PlayerParams playerParams = new PlayerParams(null, 25);
        // Act
        ResponseEntity<PlayerDto> response = restTemplate.postForEntity("/api/v1/players", playerParams, PlayerDto.class);
        // Assert
        assert response.getStatusCode().is4xxClientError();
    }

    @Test
    public void postPlayer_InvalidAge_ShouldReturnBadRequest() {
        // Arrange
        PlayerParams playerParams = new PlayerParams("Charlie", 5);
        // Act
        ResponseEntity<PlayerDto> response = restTemplate.postForEntity("/api/v1/players", playerParams, PlayerDto.class);
        // Assert
        assert response.getStatusCode().is4xxClientError();
    }

    @Test
    public void getPlayerById_ShouldReturnPlayer() {
        // Arrange
        PlayerParams playerParams = new PlayerParams("Diana", 30);
        ResponseEntity<PlayerDto> postResponse = restTemplate.postForEntity("/api/v1/players", playerParams, PlayerDto.class);
        PlayerDto createdPlayer = postResponse.getBody();
        assert createdPlayer != null;
        int playerId = createdPlayer.getId();
        // Act
        ResponseEntity<PlayerDto> getResponse = restTemplate.getForEntity("/api/v1/players/" + playerId, PlayerDto.class);
        // Assert
        assert getResponse.getStatusCode().is2xxSuccessful();
        PlayerDto fetchedPlayer = getResponse.getBody();
        assert fetchedPlayer != null;
        assert fetchedPlayer.getId().equals(playerId);
        assert fetchedPlayer.getName().equals("Diana");
        assert fetchedPlayer.getAge() == 30;
    }

    @Test
    public void getPlayerById_NotFound_ShouldReturnNotFound() {
        // Act
        ResponseEntity<PlayerDto> response = restTemplate.getForEntity("/api/v1/players/9999", PlayerDto.class);
        // Assert
        assert response.getStatusCode().is4xxClientError();
    }

}
