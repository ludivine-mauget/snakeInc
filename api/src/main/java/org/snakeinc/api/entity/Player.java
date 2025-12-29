package org.snakeinc.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.snakeinc.api.model.Category;
import org.snakeinc.api.model.PlayerDto;

import java.time.LocalDate;

@Entity
@Table(name = "players")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    public PlayerDto toDto() {
        return new PlayerDto(
                this.id,
                this.name,
                this.age,
                this.category,
                this.createdAt
        );
    }
}


