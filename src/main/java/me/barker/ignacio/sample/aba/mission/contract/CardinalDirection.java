package me.barker.ignacio.sample.aba.mission.contract;

import java.util.Optional;

public enum CardinalDirection {

    NORTH, EAST, SOUTH, WEST;

    /**
     * Wrap directions cyclically to turn clockwise,
     * @return Cardinal direction after rotating clockwise
     */
    public CardinalDirection rotateClockwise() {
        return Optional.of(ordinal() + 1)
            .filter(nextOrdinal -> nextOrdinal < values().length)
            .map(nextOrdinal -> values()[nextOrdinal])
            .orElse(NORTH);
    }

    /**
     * Wrap directions cyclically to turn anti-clockwise,
     * @return Cardinal direction after rotating anti-clockwise
     */
    public CardinalDirection rotateAnticlockwise() {
        return Optional.of(ordinal() > 0)
            .filter(Boolean::booleanValue)
            .map(x -> values()[ordinal() - 1])
            .orElse(WEST);
    }

}
