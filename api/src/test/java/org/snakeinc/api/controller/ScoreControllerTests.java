package org.snakeinc.api.controller;

import org.junit.jupiter.api.Test;
import org.snakeinc.api.ApiApplication;
import org.snakeinc.api.entity.Player;
import org.snakeinc.api.model.Category;
import org.snakeinc.api.model.ScoreDto;
import org.snakeinc.api.model.ScoreParams;
import org.snakeinc.api.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;


@SpringBootTest(classes = ApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ScoreControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PlayerRepository playerRepository;

    private Player createPlayer(String name, int age) {
        Player player = new Player();
        player.setName(name);
        player.setAge(age);
        player.setCategory(Category.JUNIOR);
        player.setCreatedAt(LocalDate.now());
        return playerRepository.save(player);
    }

    private ScoreParams createScoreParams(String snake, int score, Integer playerId) {
        return new ScoreParams(
                snake,
                score,
                LocalDateTime.parse("2025-12-05T00:00"),
                playerId
        );
    }

    @Test
    public void testCreateScore_Success() {
        // Arrange
        Player player = createPlayer("John Doe", 25);
        ScoreParams params = createScoreParams("python", 125, player.getId());

        // Act
        ResponseEntity<ScoreDto> response = restTemplate.postForEntity("/api/v1/scores", params, ScoreDto.class);

        // Assert
        assert response.getStatusCode().is2xxSuccessful();
        ScoreDto createdScore = response.getBody();
        assert createdScore != null;
        assert createdScore.getId() != null;
        assert createdScore.getSnake().equals("python");
        assert createdScore.getScore() == 125;
        assert createdScore.getPlayerId().equals(player.getId());
    }

    @Test
    public void testCreateScore_NegativeScore() {
        // Arrange
        Player player = createPlayer("Jane Doe", 30);
        ScoreParams params = createScoreParams("anaconda", -10, player.getId());

        // Act
        ResponseEntity<ScoreDto> response = restTemplate.postForEntity("/api/v1/scores", params, ScoreDto.class);

        // Assert
        assert response.getStatusCode().is4xxClientError();
    }

    @Test
    public void testCreateScore_PlayerNotFound() {
        // Arrange
        ScoreParams params = createScoreParams("python", 100, 99999);

        // Act
        ResponseEntity<ScoreDto> response = restTemplate.postForEntity("/api/v1/scores", params, ScoreDto.class);

        // Assert
        assert response.getStatusCode().is4xxClientError();
    }

    @Test
    public void testCreateScore_InvalidSnakeType() {
        // Arrange
        Player player = createPlayer("Bob Smith", 40);
        ScoreParams params = createScoreParams("invalidSnake", 100, player.getId());

        // Act
        ResponseEntity<ScoreDto> response = restTemplate.postForEntity("/api/v1/scores", params, ScoreDto.class);

        // Assert
        assert response.getStatusCode().is4xxClientError();
    }

    @Test
    public void testCreateScore_AllSnakeTypes() {
        // Arrange
        Player player = createPlayer("Alice", 28);
        String[] snakeTypes = {"python", "anaconda", "boaConstrictor"};

        for (String snakeType : snakeTypes) {
            ScoreParams params = createScoreParams(snakeType, 100, player.getId());

            // Act
            ResponseEntity<ScoreDto> response = restTemplate.postForEntity("/api/v1/scores", params, ScoreDto.class);

            // Assert
            assert response.getStatusCode().is2xxSuccessful();
            ScoreDto createdScore = response.getBody();
            assert createdScore != null;
            assert createdScore.getSnake().equals(snakeType);
        }
    }
}

