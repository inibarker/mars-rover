package me.barker.ignacio.sample.aba.mission.mars;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import me.barker.ignacio.sample.aba.mission.contract.MissionInterface;
import me.barker.ignacio.sample.aba.mission.contract.MissionStatus;
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

}
