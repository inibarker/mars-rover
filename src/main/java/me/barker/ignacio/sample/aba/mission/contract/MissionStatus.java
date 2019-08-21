package me.barker.ignacio.sample.aba.mission.contract;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Mission status report
 */
public interface MissionStatus {

    /**
     * Returns last executed command.
     */
    ControlCommand lastCommand();

    /**
     * Terrain details.
     */
    MissionTerrain terrain();

    /**
     * Rover status details.
     */
    MissionRover rover();

    static MissionStatus of(final MissionTerrain terrainParam,
                            final MissionRover roverParam,
                            final ControlCommand commandParam) {
        return new MissionStatus() {

            @Getter
            @Accessors(fluent = true)
            private ControlCommand lastCommand = commandParam;

            @Getter
            @Accessors(fluent = true)
            public MissionTerrain terrain = terrainParam;

            @Getter
            @Accessors(fluent = true)
            public MissionRover rover = roverParam;
        };
    }

}
