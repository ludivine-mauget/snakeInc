package org.snakeinc.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDto {
    private Integer id;
    private String snake;
    private int score;
    private LocalDateTime playedAt;
    private Integer playerId;
}

