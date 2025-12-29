package org.snakeinc.api.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PlayerDto {
        private Integer id;
        private String name;
        private int age;
        private Category category;
        private LocalDate createdAt;

        public PlayerDto(Integer id, String name, int age, Category category, LocalDate createdAt) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.category = category;
            this.createdAt = createdAt;
        }
}

