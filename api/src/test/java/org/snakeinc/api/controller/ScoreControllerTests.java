package org.snakeinc.api.controller;

import org.junit.jupiter.api.Test;
import org.snakeinc.api.ApiApplication;
import org.snakeinc.api.entity.Player;
import org.snakeinc.api.model.*;
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

    @Test
    public void testGetScores_WithoutFilters() {
        // Arrange
        Player player = createPlayer("Test Player", 25);
        ScoreParams params1 = createScoreParams("python", 100, player.getId());
        ScoreParams params2 = createScoreParams("anaconda", 200, player.getId());

        restTemplate.postForEntity("/api/v1/scores", params1, ScoreDto.class);
        restTemplate.postForEntity("/api/v1/scores", params2, ScoreDto.class);

        // Act
        ResponseEntity<ScoreListDto> response = restTemplate.getForEntity("/api/v1/scores", ScoreListDto.class);

        // Assert
        assert response.getStatusCode().is2xxSuccessful();
        ScoreListDto scoreList = response.getBody();
        assert scoreList != null;
        assert scoreList.getScores() != null;
        assert scoreList.getScores().size() >= 2;
    }

    @Test
    public void testGetScores_FilterBySnake() {
        // Arrange
        Player player = createPlayer("Snake Lover", 30);
        ScoreParams params1 = createScoreParams("python", 150, player.getId());
        ScoreParams params2 = createScoreParams("anaconda", 250, player.getId());

        restTemplate.postForEntity("/api/v1/scores", params1, ScoreDto.class);
        restTemplate.postForEntity("/api/v1/scores", params2, ScoreDto.class);

        // Act
        ResponseEntity<ScoreListDto> response = restTemplate.getForEntity("/api/v1/scores?snake=python", ScoreListDto.class);

        // Assert
        assert response.getStatusCode().is2xxSuccessful();
        ScoreListDto scoreList = response.getBody();
        assert scoreList != null;
        assert scoreList.getScores().stream().allMatch(s -> s.getSnake().equals("python"));
    }

    @Test
    public void testGetScores_FilterByPlayer() {
        // Arrange
        Player player1 = createPlayer("Player One", 25);
        Player player2 = createPlayer("Player Two", 30);

        ScoreParams params1 = createScoreParams("python", 100, player1.getId());
        ScoreParams params2 = createScoreParams("anaconda", 200, player2.getId());

        restTemplate.postForEntity("/api/v1/scores", params1, ScoreDto.class);
        restTemplate.postForEntity("/api/v1/scores", params2, ScoreDto.class);

        // Act
        ResponseEntity<ScoreListDto> response = restTemplate.getForEntity("/api/v1/scores?player=" + player1.getId(), ScoreListDto.class);

        // Assert
        assert response.getStatusCode().is2xxSuccessful();
        ScoreListDto scoreList = response.getBody();
        assert scoreList != null;
        assert scoreList.getScores().stream().allMatch(s -> s.getPlayerId().equals(player1.getId()));
    }

    @Test
    public void testGetPlayerStats_Success() {
        // Arrange
        Player player = createPlayer("Stats Player", 28);

        // Créer plusieurs scores pour différents types de serpents
        restTemplate.postForEntity("/api/v1/scores", createScoreParams("python", 100, player.getId()), ScoreDto.class);
        restTemplate.postForEntity("/api/v1/scores", createScoreParams("python", 200, player.getId()), ScoreDto.class);
        restTemplate.postForEntity("/api/v1/scores", createScoreParams("python", 150, player.getId()), ScoreDto.class);

        restTemplate.postForEntity("/api/v1/scores", createScoreParams("anaconda", 300, player.getId()), ScoreDto.class);
        restTemplate.postForEntity("/api/v1/scores", createScoreParams("anaconda", 400, player.getId()), ScoreDto.class);

        // Act
        ResponseEntity<PlayerStatsDto> response = restTemplate.getForEntity("/api/v1/scores/stats?playerId=" + player.getId(), PlayerStatsDto.class);

        // Assert
        assert response.getStatusCode().is2xxSuccessful();
        PlayerStatsDto stats = response.getBody();
        assert stats != null;
        assert stats.getPlayerId().equals(player.getId());
        assert stats.getStats() != null;
        assert stats.getStats().size() == 2; // python et anaconda

        // Vérifier les stats pour python
        SnakeStatsDto pythonStats = stats.getStats().stream()
                .filter(s -> s.getSnake().equals("python"))
                .findFirst()
                .orElse(null);
        assert pythonStats != null;
        assert pythonStats.getMin() == 100;
        assert pythonStats.getMax() == 200;
        assert pythonStats.getAverage() == 150.0;

        // Vérifier les stats pour anaconda
        SnakeStatsDto anacondaStats = stats.getStats().stream()
                .filter(s -> s.getSnake().equals("anaconda"))
                .findFirst()
                .orElse(null);
        assert anacondaStats != null;
        assert anacondaStats.getMin() == 300;
        assert anacondaStats.getMax() == 400;
        assert anacondaStats.getAverage() == 350.0;
    }

    @Test
    public void testGetPlayerStats_PlayerNotFound() {
        // Act
        ResponseEntity<PlayerStatsDto> response = restTemplate.getForEntity("/api/v1/scores/stats?playerId=99999", PlayerStatsDto.class);

        // Assert
        assert response.getStatusCode().is4xxClientError();
    }
}

