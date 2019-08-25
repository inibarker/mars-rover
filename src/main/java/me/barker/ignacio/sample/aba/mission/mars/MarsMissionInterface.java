package me.barker.ignacio.sample.aba.mission.mars;

import javax.annotation.PostConstruct;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import me.barker.ignacio.sample.aba.mission.contract.MissionInterface;
import me.barker.ignacio.sample.aba.mission.contract.MissionRover;
import me.barker.ignacio.sample.aba.mission.contract.MissionStatus;
import me.barker.ignacio.sample.aba.mission.contract.MissionTerrain;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(MarsMissionContextProperties.class)
public class MarsMissionInterface implements MissionInterface {

    @Getter(AccessLevel.PACKAGE)
    private MissionTerrain missionTerrain;

    @Getter(AccessLevel.PACKAGE)
    private MissionRover missionRover;

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
        return Mono.just(MissionStatus.of(missionTerrain, missionRover, ControlCommand.MissionCommand.REPORT));
    }
}
