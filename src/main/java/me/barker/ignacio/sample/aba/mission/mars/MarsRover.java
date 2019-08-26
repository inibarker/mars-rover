package me.barker.ignacio.sample.aba.mission.mars;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

import lombok.Builder;
import lombok.Data;
import lombok.val;
import me.barker.ignacio.sample.aba.mission.contract.CardinalDirection;
import me.barker.ignacio.sample.aba.mission.contract.MissionRover;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;

@Data
@Builder(toBuilder = true)
public class MarsRover implements MissionRover {

    @Builder.Default
    private Pair<Integer, Integer> position = Pair.of(0, 0);

    @Builder.Default
    private CardinalDirection facing = CardinalDirection.NORTH;

    @Override
    public void move(final boolean backwards) {
        if (!ObjectUtils.allNotNull(getPosition(), getFacing())) return;
        Optional.of(getFacing())
            .filter(Predicate.isEqual(CardinalDirection.NORTH)
                .or(Predicate.isEqual(CardinalDirection.SOUTH)))
            .map(cardinalDirection -> (Consumer<Integer>) factor ->
                setPosition(Pair.of(getPosition().getLeft() + factor, getPosition().getRight())))
            .orElse(factor ->
                setPosition(Pair.of(getPosition().getLeft(), getPosition().getRight() + factor)))
            .accept(getFactor(backwards));
    }

    int getFactor(final boolean backwards) {
        val isNorthOrEast = Predicate.isEqual(CardinalDirection.NORTH)
            .or(Predicate.isEqual(CardinalDirection.EAST));

        return Optional.of(getFacing())
            .filter(isNorthOrEast.and(x -> backwards)
                .or(isNorthOrEast.negate().and(x -> !backwards)))
            .map(x -> -1)
            .orElse(1);
    }

    @Override
    public void turn(final boolean clockwise) {
        if (getFacing() == null) return;
        setFacing(clockwise ? getFacing().rotateClockwise() : getFacing().rotateAnticlockwise());
    }
}
