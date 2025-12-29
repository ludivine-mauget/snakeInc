package org.snakeinc.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.snakeinc.api.model.ScoreDto;
import org.snakeinc.api.model.SnakeType;

import java.time.LocalDateTime;

@Entity
@Table(name = "scores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SnakeType snake;

    @Column(nullable = false)
    private int score;

    @Column(name = "played_at", nullable = false)
    private LocalDateTime playedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    public ScoreDto toDto() {
        return new ScoreDto(
                this.id,
                this.snake.getValue(),
                this.score,
                this.playedAt,
                this.player.getId()
        );
    }
}

