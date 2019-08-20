package me.barker.ignacio.sample.aba.mission.contract;

/**
 * Mission status report
 */
public interface MissionStatus {

    /**
     * Returns last executed command.
     */
    ControlCommand lastCommand();

    /**
     * Returns last command execution status.
     */
    boolean commandProcessed();

    /**
     * Terrain details.
     */
    MissionTerrain terrain();

    /**
     * Rover status details.
     */
    MissionRover rover();

}
