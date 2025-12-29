package org.snakeinc.api.model;

public enum SnakeType {
    PYTHON("python"),
    ANACONDA("anaconda"),
    BOA_CONSTRICTOR("boaConstrictor");

    private final String value;

    SnakeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SnakeType fromValue(String value) {
        for (SnakeType type : SnakeType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid snake type: " + value);
    }
}
