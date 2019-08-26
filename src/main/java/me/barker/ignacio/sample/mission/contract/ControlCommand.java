package me.barker.ignacio.sample.mission.contract;

import lombok.ToString;

/**
 * Mission Control Command
 */
@ToString
public enum ControlCommand {

    MOVE_FORWARDS, MOVE_BACKWARDS, TURN_LEFT, TURN_RIGHT, REPORT, QUIT;

}
