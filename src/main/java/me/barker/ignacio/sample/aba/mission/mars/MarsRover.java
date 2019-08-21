package me.barker.ignacio.sample.aba.mission.mars;

import lombok.Data;
import lombok.var;
import me.barker.ignacio.sample.aba.mission.contract.CardinalDirection;
import me.barker.ignacio.sample.aba.mission.contract.MissionRover;
import org.apache.commons.lang3.tuple.Pair;

@Data
public class MarsRover implements MissionRover {

    private Pair<Integer, Integer> position;

    private CardinalDirection facing;

    @Override
    public void move(final boolean backwards) {
        if (position == null || facing == null) return;
        var factor = backwards ? -1 : 1;
        if (CardinalDirection.SOUTH.equals(facing)
                || CardinalDirection.WEST.equals(facing)) {
            factor = 0 - factor;
        }
        if (CardinalDirection.NORTH.equals(facing)
                || CardinalDirection.SOUTH.equals(facing)) {
            position = Pair.of(position.getLeft() + factor, position.getRight());
        } else {
            position = Pair.of(position.getLeft(), position.getRight() + factor);
        }
    }

    @Override
    public void turn(final boolean clockwise) {
        if (facing == null) return;
        facing = clockwise ? facing.rotateClockwise() : facing.rotateAnticlockwise();
    }
}
