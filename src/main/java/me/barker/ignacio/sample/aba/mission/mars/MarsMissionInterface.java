package me.barker.ignacio.sample.aba.mission.mars;

import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import me.barker.ignacio.sample.aba.mission.contract.MissionInterface;
import me.barker.ignacio.sample.aba.mission.contract.MissionStatus;
import org.apache.commons.lang3.NotImplementedException;
import reactor.core.publisher.Mono;

public class MarsMissionInterface implements MissionInterface {

    @Override
    public Mono<MissionStatus> operate(final ControlCommand command) {
        // TODO Implement
        throw new NotImplementedException("Implement");
    }

    @Override
    public Mono<MissionStatus> report() {
        // TODO Implement
        throw new NotImplementedException("Implement");
    }

}
