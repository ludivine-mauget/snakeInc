package org.snakeinc.api.service;

import org.snakeinc.api.entity.Player;
import org.snakeinc.api.entity.Score;
import org.snakeinc.api.exception.InvalidSnakeTypeException;
import org.snakeinc.api.exception.PlayerNotFoundException;
import org.snakeinc.api.model.ScoreDto;
import org.snakeinc.api.model.ScoreListDto;
import org.snakeinc.api.model.ScoreParams;
import org.snakeinc.api.model.SnakeType;
import org.snakeinc.api.repository.PlayerRepository;
import org.snakeinc.api.repository.ScoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final PlayerRepository playerRepository;

    public ScoreService(ScoreRepository scoreRepository, PlayerRepository playerRepository) {
        this.scoreRepository = scoreRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional
    public ScoreDto createScore(ScoreParams params) {
        Player player = playerRepository.findById(params.playerId())
                .orElseThrow(() -> new PlayerNotFoundException(params.playerId()));

        SnakeType snakeType;
        try {
            snakeType = SnakeType.fromValue(params.snake());
        } catch (IllegalArgumentException e) {
            throw new InvalidSnakeTypeException(params.snake());
        }

        Score score = new Score();
        score.setSnake(snakeType);
        score.setScore(params.score());
        score.setPlayedAt(params.playedAt());
        score.setPlayer(player);

        Score savedScore = scoreRepository.save(score);

        return savedScore.toDto();
    }

    @Transactional(readOnly = true)
    public ScoreListDto getScores(String snake, Integer playerId) {
        List<Score> scores;

        if (snake != null && playerId != null) {
            SnakeType snakeType;
            try {
                snakeType = SnakeType.fromValue(snake);
            } catch (IllegalArgumentException e) {
                throw new InvalidSnakeTypeException(snake);
            }
            scores = scoreRepository.findBySnakeAndPlayerId(snakeType, playerId);
        } else if (snake != null) {
            SnakeType snakeType;
            try {
                snakeType = SnakeType.fromValue(snake);
            } catch (IllegalArgumentException e) {
                throw new InvalidSnakeTypeException(snake);
            }
            scores = scoreRepository.findBySnake(snakeType);
        } else if (playerId != null) {
            scores = scoreRepository.findByPlayerId(playerId);
        } else {
            scores = scoreRepository.findAll();
        }

        List<ScoreDto> scoreDtos = scores.stream()
                .map(Score::toDto)
                .collect(Collectors.toList());

        return new ScoreListDto(scoreDtos);
    }
}

