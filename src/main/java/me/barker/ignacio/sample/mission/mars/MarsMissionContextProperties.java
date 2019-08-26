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
            .dimensions(Pair.of(abs(getTerrain().getHeight()), abs(getTerrain().getWidth())))
            .wrapping(Pair.of(getTerrain().getHWrap(), getTerrain().getHWrap()))
            .obstacles(getTerrain().getObstacles())
            .build();
    }

    public MarsRover extractRoverData() {
        return MarsRover.builder()
            .position(Pair.of(getRover().getParallel(), getRover().getMeridian()))
            .facing(getRover().getDirection())
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
