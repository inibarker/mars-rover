package me.barker.ignacio.sample.mission.contract;

import reactor.core.publisher.Mono;

/**
 * Space Exploration Mission
 */
public interface MissionInterface {

    /**
     * Operates the mission
     * @param command Command to operate the mission
     * @return New mission status since last operation
     */
    Mono<MissionStatus> operate(final ControlCommand command);

    /**
     * Report mission status.
     */
    Mono<MissionStatus> report();
}
