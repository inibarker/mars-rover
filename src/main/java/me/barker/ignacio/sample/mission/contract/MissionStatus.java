package me.barker.ignacio.sample.mission.contract;

import lombok.ToString;
import lombok.Value;
import lombok.experimental.Accessors;

/**
 * Mission status report
 */
public interface MissionStatus {

    /**
     * Returns last executed command.
     */
    <T extends ControlCommand> T lastCommand();

    /**
     * Terrain details.
     */
    <T extends MissionTerrain> T terrain();

    /**
     * Rover status details.
     */
    <T extends MissionRover> T rover();

    static MissionStatus of(final MissionTerrain terrainParam,
                            final MissionRover roverParam,
                            final ControlCommand commandParam) {
        return new DefaultMissionStatus(commandParam, terrainParam, roverParam);
    }

    @Value
    @ToString
    @Accessors(fluent = true)
    class DefaultMissionStatus implements MissionStatus {
        private ControlCommand lastCommand;
        public MissionTerrain terrain;
        public MissionRover rover;
    }
}
