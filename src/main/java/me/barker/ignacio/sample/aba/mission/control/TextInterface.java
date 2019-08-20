package me.barker.ignacio.sample.aba.mission.control;

import me.barker.ignacio.sample.aba.mission.contract.ControlCommand;
import me.barker.ignacio.sample.aba.mission.contract.ControlInterface;
import me.barker.ignacio.sample.aba.mission.contract.MissionStatus;
import me.barker.ignacio.sample.aba.mission.contract.ReportingInterface;
import org.apache.commons.lang3.NotImplementedException;
import reactor.core.publisher.Mono;

public class TextInterface implements ReportingInterface, ControlInterface {

    @Override
    public void report(final MissionStatus report) {
        // TODO Implement
        throw new NotImplementedException("Implement");
    }

    @Override
    public Mono<ControlCommand> nextCommand() {
        // TODO Implement
        throw new NotImplementedException("Implement");
    }

    @Override
    public ControlCommand lastCommand() {
        // TODO Implement
        throw new NotImplementedException("Implement");
    }
}
