package org.snakeinc.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatsDto {
    private Integer playerId;
    private List<SnakeStatsDto> stats;
}

