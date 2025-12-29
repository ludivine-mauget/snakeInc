package org.snakeinc.api.repository;

import org.snakeinc.api.entity.Score;
import org.snakeinc.api.model.SnakeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {
    List<Score> findBySnake(SnakeType snake);
    List<Score> findByPlayerId(Integer playerId);
    List<Score> findBySnakeAndPlayerId(SnakeType snake, Integer playerId);

    @Query("SELECT s.snake, MIN(s.score), MAX(s.score), AVG(s.score) " +
           "FROM Score s WHERE s.player.id = :playerId " +
           "GROUP BY s.snake")
    List<Object[]> findStatsByPlayerId(@Param("playerId") Integer playerId);
}

