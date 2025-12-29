package org.snakeinc.api.model;

import java.time.LocalDate;

public record Player(
        Integer id,
        String name,
        int age,
        Category category,
        LocalDate createdAt
) {
}

