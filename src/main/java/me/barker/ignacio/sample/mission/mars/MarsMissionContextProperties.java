package me.barker.ignacio.sample.mission.mars;

import java.util.Map;
import java.util.Set;

import lombok.Data;
import me.barker.ignacio.sample.mission.contract.CardinalDirection;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static java.lang.Math.abs;

@Data
@ConfigurationProperties("mission.mars")
public class MarsMissionContextProperties {

    private TerrainDto terrain;

    private RoverDto rover;

    public MarsTerrain extractTerrainData() {
        return MarsTerrain.builder()
            .dimensions(Pair.of(abs(terrain.getHeight()), abs(terrain.getWidth())))
            .wrapping(Pair.of(terrain.getHWrap(), terrain.getHWrap()))
            .obstacles(terrain.getObstacles())
            .build();
    }

    public MarsRover extractRoverData() {
        return MarsRover.builder()
            .position(Pair.of(rover.getParallel(), rover.getMeridian()))
            .facing(rover.getDirection())
            .build();
    }

    @Data
    private static class TerrainDto {
        private Integer height;
        private Integer width;
        private Boolean hWrap;
        private Boolean wWrap;
        private Map<Integer, Set<Integer>> obstacles;
    }

    @Data
    private static class RoverDto {
        private Integer parallel;
        private Integer meridian;
        private CardinalDirection direction;
    }
}
