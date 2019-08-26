package me.barker.ignacio.sample.mission.control;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Value;
import lombok.experimental.Accessors;
import me.barker.ignacio.sample.mission.contract.CardinalDirection;
import me.barker.ignacio.sample.mission.contract.ControlCommand;
import me.barker.ignacio.sample.mission.contract.MissionRover;
import me.barker.ignacio.sample.mission.contract.MissionStatus;
import me.barker.ignacio.sample.mission.contract.MissionTerrain;
import org.apache.commons.lang3.tuple.Pair;

@Value
@Accessors(fluent = true)
class MissionStatusStub implements MissionStatus {

    ControlCommand lastCommand = ControlCommand.REPORT;

    boolean commandProcessed = true;

    MissionTerrain terrain = new MissionTerrain() {
        @Override
        public Pair<Integer, Integer> getDimensions() {
            return Pair.of(1, 1);
        }
        @Override
        public Pair<Boolean, Boolean> getWrapping() {
            return Pair.of(false, false);
        }
        @Override
        public Map<Integer, Set<Integer>> getObstacles() {
            return new HashMap<>();
        }
    };

    MissionRover rover = new MissionRover() {

        @Override
        public Pair<Integer, Integer> getPosition() {
            return Pair.of(1, 1);
        }

        @Override
        public CardinalDirection getFacing() {
            return CardinalDirection.NORTH;
        }

        @Override
        public void move(final boolean backwards) {

        }

        @Override
        public void turn(final boolean clockwise) {

        }
    };

}
