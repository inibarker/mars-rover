package me.barker.ignacio.sample.mission.mars;

import java.util.Collections;
import java.util.Optional;
import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import me.barker.ignacio.sample.mission.contract.ControlCommand;
import me.barker.ignacio.sample.mission.contract.MissionInterface;
import me.barker.ignacio.sample.mission.contract.MissionRover;
import me.barker.ignacio.sample.mission.contract.MissionStatus;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static me.barker.ignacio.sample.mission.contract.ControlCommand.RoverCommand.MOVE_BACKWARDS;
import static me.barker.ignacio.sample.mission.contract.ControlCommand.RoverCommand.MOVE_FORWARDS;
import static me.barker.ignacio.sample.mission.contract.ControlCommand.RoverCommand.TURN_LEFT;
import static me.barker.ignacio.sample.mission.contract.ControlCommand.RoverCommand.TURN_RIGHT;

@Log4j2
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(MarsMissionContextProperties.class)
public class MarsMissionInterface implements MissionInterface {

    private final MarsMissionContextProperties marsMissionContextProperties;

    private MarsTerrain missionTerrain;

    private MarsRover missionRover;
    
    @PostConstruct
    public void setUp() {
        missionTerrain = marsMissionContextProperties.extractTerrainData();
        missionRover = marsMissionContextProperties.extractRoverData();
    }

    @Override
    public Mono<MissionStatus> operate(final ControlCommand command) {
        return Mono.fromRunnable(() -> operateRover(command))
            .then(buildReport(command));
    }

    @Override
    public Mono<MissionStatus> report() {
        return buildReport(ControlCommand.MissionCommand.REPORT);
    }

    Mono<MissionStatus> buildReport(final ControlCommand missionCommand) {
        return Mono.fromCallable(() -> MissionStatus.of(missionTerrain, missionRover, missionCommand));
    }

    private void operateRover(final ControlCommand command) {
        if (MOVE_FORWARDS.equals(command)) {
            moveRover(false);
        } else if (MOVE_BACKWARDS.equals(command)) {
            moveRover(true);
        } else if (TURN_LEFT.equals(command)) {
            missionRover.turn(false);
        } else if (TURN_RIGHT.equals(command)) {
            missionRover.turn(true);
        }
    }

    void moveRover(final boolean backwards) {
        Optional.of(inferNewPosition(backwards))
            .filter(this::noObstaclePresent)
            .ifPresent(missionRover::setPosition);
    }

    boolean noObstaclePresent(final Pair<Integer, Integer> newPosition) {
        return !missionTerrain.getObstacles()
            .getOrDefault(newPosition.getKey(), Collections.EMPTY_SET)
            .contains(newPosition.getValue());
    }

    Pair<Integer, Integer> inferNewPosition(final boolean backwards) {
        val pointerRover = missionRover.toBuilder().build();
        pointerRover.move(backwards);
        return Optional.of(pointerRover)
            .map(MissionRover::getPosition)
            .map(newPosition -> Pair.of(
                wrapIntoDimension(newPosition.getLeft(),
                    missionTerrain.getWrapping().getLeft(),
                    missionTerrain.getDimensions().getLeft()),
                wrapIntoDimension(newPosition.getRight(),
                    missionTerrain.getWrapping().getRight(),
                    missionTerrain.getDimensions().getRight())))
            .get();
    }

    static int wrapIntoDimension(final int newPosition,
                                 final boolean withWrapping,
                                 final int dimensionLength) {
        if (newPosition < 0) {
            if (withWrapping) {
                return dimensionLength - 1;
            } else {
                return 0;
            }
        } else if (newPosition >= dimensionLength) {
            if (withWrapping) {
                return 0;
            } else {
                return dimensionLength - 1;
            }
        }
        return newPosition;
    }

}
