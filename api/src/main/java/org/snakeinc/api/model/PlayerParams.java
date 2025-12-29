package org.snakeinc.api.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PlayerParams(
        @NotNull
        String name,
        @Min(14)
        int age) {
}
