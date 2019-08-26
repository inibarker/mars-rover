package me.barker.ignacio.sample.mission.contract;

import lombok.ToString;

/**
 * Mission Control Command
 */
public interface ControlCommand {

    @ToString
    enum RoverCommand implements ControlCommand {
        MOVE_FORWARDS, MOVE_BACKWARDS, TURN_LEFT, TURN_RIGHT
    }

    @ToString
    enum MissionCommand implements ControlCommand {
        REPORT, WAIT, QUIT
    }

}
