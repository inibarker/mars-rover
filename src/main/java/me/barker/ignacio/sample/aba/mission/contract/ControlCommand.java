package me.barker.ignacio.sample.aba.mission.contract;

/**
 * Mission Control Command
 */
public interface ControlCommand {

    enum Status {
        CREATED, SENDING, RECEIVED, PROCESSING, FINISHED;
    }

    enum RoverCommand implements ControlCommand {
        FORWARD, BACKWARD, LEFT, RIGHT
    }

    enum MissionCommand implements ControlCommand {
        REPORT, WAIT, QUIT
    }

}
