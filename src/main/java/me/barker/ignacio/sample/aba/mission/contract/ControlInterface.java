package me.barker.ignacio.sample.aba.mission.contract;

import reactor.core.publisher.Mono;

/**
 * Mission Control Center Interface
 */
public interface ControlInterface {

    /**
     * Returns next pending command
     * @return An optional with next command or empty if there are no pending command
     */
    Mono<ControlCommand> nextCommand();

    /**
     * Returns last command emitted by {@linkplain #nextCommand()}
     */
    ControlCommand lastCommand();
}
