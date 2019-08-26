package me.barker.ignacio.sample.mission.mars;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import me.barker.ignacio.sample.mission.contract.ControlCommand;
import me.barker.ignacio.sample.mission.contract.MissionInterface;
import me.barker.ignacio.sample.mission.contract.MissionStatus;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(MarsMissionContextProperties.class)
public class MarsMissionInterface implements MissionInterface {

    private MarsTerrain missionTerrain;

    private MarsRover missionRover;

    private final MarsMissionContextProperties marsMissionContextProperties;

    @PostConstruct
    public void setUp() {
        missionTerrain = marsMissionContextProperties.extractTerrainData();
        missionRover = marsMissionContextProperties.extractRoverData();
    }

    @Override
    public Mono<MissionStatus> operate(final ControlCommand command) {
        // TODO Implement
        throw new NotImplementedException("Implement");
    }

    @Override
    public Mono<MissionStatus> report() {
        return buildReport(ControlCommand.MissionCommand.REPORT);
    }

    Mono<MissionStatus> buildReport(final ControlCommand missionCommand) {
        return Mono.fromCallable(() -> MissionStatus.of(missionTerrain, missionRover, missionCommand));
    }

    void moveRover(final boolean backwards) {
    }

    boolean noObstaclePresent(final Pair<Integer, Integer> position) {
        return false;
    }

    Pair<Integer, Integer> inferNewPosition(final boolean backwards) {
        return null;
    }

    static int wrapIntoDimension(final int position,
                                 final boolean backwards,
                                 final int bound) {
        return 0;
    }

}
