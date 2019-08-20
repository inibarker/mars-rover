package me.barker.ignacio.sample.aba.mission.mars;

import lombok.Data;
import me.barker.ignacio.sample.aba.mission.contract.CardinalDirection;
import me.barker.ignacio.sample.aba.mission.contract.MissionRover;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;

@Data
public class MarsRover implements MissionRover {

    private Pair<Integer, Integer> position;

    private CardinalDirection facing;

    @Override
    public boolean move(final boolean backwards) {
        // TODO Implement
        throw new NotImplementedException("Implement");
    }

    @Override
    public boolean turn(final boolean clockwise) {
        // TODO Implement
        throw new NotImplementedException("Implement");
    }
}
