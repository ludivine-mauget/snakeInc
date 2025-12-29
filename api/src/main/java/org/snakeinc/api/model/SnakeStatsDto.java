package org.snakeinc.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnakeStatsDto {
    private String snake;
    private Integer min;
    private Integer max;
    private Double average;
}
